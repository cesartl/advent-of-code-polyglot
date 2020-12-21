package com.ctl.aoc.kotlin.y2020

object Day21 {

    fun solve1(input: Sequence<String>): Int {
        val inputs = input.map { Input.parse(it) }.toList()
        val noAllergen = findNoAllergen(inputs)
        return inputs.map { (ingredients, _) -> ingredients.count { ingredient -> noAllergen.contains(ingredient) } }.sum()
    }

    fun solve2(input: Sequence<String>): String {
        val inputs = input.map { Input.parse(it) }.toList()
        val noAllergen = findNoAllergen(inputs).toSet()
        val validIngredients = inputs.flatMap { it.ingredients }.filter { !noAllergen.contains(it) }.toSet()
        val allergens = inputs.flatMap { it.allergens }.toSet()
        var assignments = allCombinations(validIngredients, allergens).filter { it.isValid(inputs) }.toList()

        val finalAssignments = mutableMapOf<String, String>()
        while (finalAssignments.size < validIngredients.size) {
            val byIngredients = assignments.groupBy { it.ingredient }
            byIngredients.filterValues { it.size == 1 }.forEach { (ingredient, allergens) ->
                finalAssignments[ingredient] = allergens.first().allergen
            }
            assignments = assignments.filter { !finalAssignments.contains(it.ingredient) && !finalAssignments.containsValue(it.allergen) }
        }
        println(finalAssignments)
        return finalAssignments.toList().sortedBy { it.second }.map { it.first }.joinToString(",")
    }

    data class Assignment(val ingredient: String, val allergen: String) {
        fun isValid(inputs: List<Input>): Boolean {
            return inputs.all { (ingredients, allergens) ->
                if (allergens.contains(allergen)) {
                    ingredients.contains(ingredient)
                } else {
                    true
                }
            }
        }
    }

    private fun allCombinations(validIngredients: Set<String>, allergens: Set<String>): Sequence<Assignment> = sequence {
        validIngredients.forEach { ingredient ->
            allergens.forEach { allergen ->
                yield(Assignment(ingredient, allergen))
            }
        }
    }

    private fun findNoAllergen(inputs: List<Input>): List<String> {
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
        return ingredientToAllergens.keys.filter { ingredient ->
            !(ingredientToAllergens[ingredient] ?: mutableSetOf()).any { allergen ->
                (allergenToInput[allergen] ?: mutableListOf()).all { input -> input.ingredients.contains(ingredient) }
            }
        }
    }


    data class AllergenSearch(val allergens: List<Allergen>) {
        fun isIngredientAllowed(ingredient: String) = allergens.none { it is Allergen.KnownIngredient && it.ingredient == ingredient }
    }

    tailrec fun AllergenSearch.search(): AllergenSearch {
        val newAllergens = allergens.map { allergen ->
            when (allergen) {
                is Allergen.UnKnownIngredient -> allergen.copy(ingredients = allergen.ingredients.filter { isIngredientAllowed(it) })
                is Allergen.KnownIngredient -> allergen
            }
        }.map { allergen ->
            when (allergen) {
                is Allergen.UnKnownIngredient -> if (allergen.ingredients.size == 1) Allergen.KnownIngredient(allergen.name, allergen.ingredients.first()) else allergen
                is Allergen.KnownIngredient -> allergen
            }
        }
        return if (allergens == newAllergens) {
            return this
        } else {
            AllergenSearch(newAllergens).search()
        }
    }

    sealed class Allergen {
        abstract val name: String

        data class KnownIngredient(override val name: String, val ingredient: String) : Allergen()
        data class UnKnownIngredient(override val name: String, val ingredients: List<String>) : Allergen()

        companion object {
            fun build(inputs: List<Input>, noAllergen: Set<String>): List<Allergen> {
                val allergenMap = mutableMapOf<String, MutableSet<String>>()
                inputs.forEach { (ingredients, allergens) ->
                    allergens.forEach { allergen ->
                        allergenMap.computeIfAbsent(allergen) { mutableSetOf() }.addAll(ingredients.filter { !noAllergen.contains(it) })
                    }
                }
                return allergenMap.map { entry ->
                    UnKnownIngredient(entry.key, entry.value.toList())
                }
            }
        }
    }

    fun isAssignmentValid(inputs: List<Input>, ingredient: String, allergen: String): Boolean {
        return inputs.all { (ingredients, allergens) ->
            if (allergens.contains(allergen)) {
                ingredients.contains(ingredient)
            } else {
                true
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