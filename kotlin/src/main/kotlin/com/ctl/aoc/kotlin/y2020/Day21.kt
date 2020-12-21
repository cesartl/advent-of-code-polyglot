package com.ctl.aoc.kotlin.y2020

object Day21 {

    fun solve1(input: Sequence<String>): Int {
        val inputs = input.map { Input.parse(it) }.toList()
        val ingredientToAllergens: MutableMap<String, MutableSet<String>> = mutableMapOf()
        val allergenToInput: MutableMap<String, MutableList<Input>> = mutableMapOf()
        inputs.forEach { input ->
            val (ingredients, allergens) = input
            allergens.forEach { allergen ->
                allergenToInput.computeIfAbsent(allergen) { mutableListOf() }.add(input)
            }
            ingredients.forEach { ingredient ->
                ingredientToAllergens.computeIfAbsent(ingredient) { mutableSetOf() }.addAll(allergens)
            }
        }
        val noAllergen = ingredientToAllergens.keys.filter { ingredient ->
            !(ingredientToAllergens[ingredient] ?: mutableSetOf()).any { allergen ->
                (allergenToInput[allergen] ?: mutableListOf()).all { input -> input.ingredients.contains(ingredient) }
            }
        }
        return inputs.map { (ingredients, _) -> ingredients.count { ingredient -> noAllergen.contains(ingredient) } }.sum()
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }


    data class IngredientSearch(val ingredients: List<Ingredient>) {
        fun isKnownAllergen(allergen: String): Boolean {
            return ingredients.filterIsInstance(Ingredient.KnownAllergen::class.java).any { it.allergen == allergen }
        }
    }

    tailrec fun IngredientSearch.search(): IngredientSearch {
        val newIngredients = ingredients.map { ingredient ->
            when (ingredient) {
                is Ingredient.UnKnownAllergen -> ingredient.copy(allergens = ingredient.allergens.filter { !isKnownAllergen(it) })
                else -> ingredient
            }
        }.map { ingredient ->
            when (ingredient) {
                is Ingredient.UnKnownAllergen -> {
                    when (ingredient.allergens.size) {
                        0 -> Ingredient.NoAllergen(ingredient.name)
                        1 -> Ingredient.KnownAllergen(ingredient.name, ingredient.allergens.first())
                        else -> ingredient
                    }
                }
                else -> ingredient
            }
        }
        return if (newIngredients == ingredients) {
            return this
        } else {
            IngredientSearch(newIngredients).search()
        }
    }

    sealed class Ingredient {
        abstract val name: String

        data class KnownAllergen(override val name: String, val allergen: String) : Ingredient()
        data class UnKnownAllergen(override val name: String, val allergens: List<String>) : Ingredient()
        data class NoAllergen(override val name: String) : Ingredient()

        companion object {
            fun build(inputs: List<Input>): List<Ingredient> {
                val result = mutableListOf<Ingredient>()
                val ingredientMap = mutableMapOf<String, MutableList<String>>()
                inputs.forEach { (ingredients, allergens) ->
                    ingredients.forEach { ingredient ->
                        ingredientMap.computeIfAbsent(ingredient) { mutableListOf() }.addAll(allergens)
                    }
                }
                return ingredientMap.map { entry ->
                    UnKnownAllergen(entry.key, entry.value)
                }
            }
        }
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