package com.ctl.aoc.kotlin.y2020

object Day21Bis {

    fun solve1(input: Sequence<String>): Int {
        val inputs = input.map { Input.parse(it) }.toList()
        val ingredientToAllergen = findMappings(inputs)
        val noAllergen = ingredientToAllergen.filter { it.value.isEmpty() }.map { it.key }
        return inputs.map { (ingredients, _) -> ingredients.count { ingredient -> noAllergen.contains(ingredient) } }.sum()
    }

    fun solve2(input: Sequence<String>): String {
        val inputs = input.map { Input.parse(it) }.toList()
        val ingredientToAllergen = findMappings(inputs)
        val ingredients = ingredientToAllergen.filter { it.value.isNotEmpty() }
                .map { (ingredient, allergens) ->
                    Ingredient.UnKnownAllergen(ingredient, allergens.toList())
                }

        val search = IngredientSearch(ingredients).search()
        return search.ingredients
                .filterIsInstance(Ingredient.KnownAllergen::class.java)
                .sortedBy { it.allergen }.joinToString(",") { it.name }
    }

    private fun findMappings(inputs: List<Input>): Map<String, Set<String>> {
        val allIngredients = inputs.flatMap { it.ingredients }.toSet()
        val allAllergen = inputs.flatMap { it.allergens }.toSet()

        val allPossible = allIngredients.fold(mapOf<String, Set<String>>()) { acc, ingredient -> acc + (ingredient to allAllergen) }

        return inputs.fold(allPossible) { acc, (ingredients, allergens) ->
            val newMap = (allIngredients - ingredients).map { it to (acc.getValue(it) - allergens) }.toMap()
            acc + newMap
        }
    }


    data class IngredientSearch(val ingredients: List<Ingredient>) {
        fun isAllergenAllowed(allergen: String) = ingredients.none { it is Ingredient.KnownAllergen && it.allergen == allergen }
    }

    tailrec fun IngredientSearch.search(): IngredientSearch {
        val newAllergens = ingredients.map { ingredient ->
            when (ingredient) {
                is Ingredient.UnKnownAllergen -> ingredient.copy(allergens = ingredient.allergens.filter { isAllergenAllowed(it) })
                is Ingredient.KnownAllergen -> ingredient
            }
        }.map { ingredient ->
            when (ingredient) {
                is Ingredient.UnKnownAllergen -> if (ingredient.allergens.size == 1) Ingredient.KnownAllergen(ingredient.name, ingredient.allergens.first()) else ingredient
                is Ingredient.KnownAllergen -> ingredient
            }
        }
        return if (ingredients == newAllergens) {
            return this
        } else {
            IngredientSearch(newAllergens).search()
        }
    }

    sealed class Ingredient {
        abstract val name: String

        data class KnownAllergen(override val name: String, val allergen: String) : Ingredient()
        data class UnKnownAllergen(override val name: String, val allergens: List<String>) : Ingredient()
    }

    data class Input(val ingredients: List<String>, val allergens: List<String>) {

        companion object {
            val containsRegex = """\(contains ([\w ,]+)\)""".toRegex()
            fun parse(line: String): Input {
                val idx = line.indexOf("(contains ")
                if (idx < 0) {
                    error(line)
                }
                val ingredients = line.substring(0, idx - 1).split(" ").toList()
                val contains = line.substring(idx)
                val m = containsRegex.matchEntire(contains) ?: error(contains)
                val allergens = m.groupValues[1].trim().split(",").map { it.trim() }.toList()
                return Input(ingredients, allergens)
            }
        }
    }
}