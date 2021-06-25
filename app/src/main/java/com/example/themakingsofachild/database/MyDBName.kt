package com.example.themakingsofachild.database

class MyDBName {
    companion object {
        val TABLE_NAME = arrayOf("junior_group" , "middle_group", "senior_group", "preparatory_group", "first_graders")
        val TABLE_INC_NAME = arrayOf(
            "intellectual_inclinations" ,
            "logical_inclinations",
            "natural_science_inclinations",
            "inclinations_of_creative_thinking",
            "physical_education_inclinations",
            "musical_inclinations",
            "artistic_inclination",
            "leadership_leanings",
            "philological_inclinations")
        const val COLUMN_NAME = "full_name"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_INC_NAME = "inclination"

        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "children.db"

        val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${MyDBName.TABLE_NAME}"
    }
}