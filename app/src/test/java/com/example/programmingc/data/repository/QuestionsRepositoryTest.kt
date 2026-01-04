package com.example.programmingc.data.repository

import android.content.Context
import android.content.res.AssetManager
import com.example.programmingc.domain.model.QuestionType
import com.example.programmingc.domain.model.QuizQuestion
import org.junit.After
import org.junit.Before
import org.junit.Test
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import java.io.ByteArrayInputStream
import java.io.FileNotFoundException
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class QuestionsRepositoryTest {
    // Мокаем зависимости
    private lateinit var mockContext: Context
    private lateinit var mockAssetManager: AssetManager
    private lateinit var repository: QuestionsRepository

    // Для тестирования корутин
    private var testDispatcher = StandardTestDispatcher()
    private var testScope = TestScope(testDispatcher)

    @Before
    fun setUp(){
        MockKAnnotations.init(this, relaxUnitFun = true)

        mockContext = mockk()
        mockAssetManager = mockk()

        every { mockContext.assets } returns mockAssetManager

        repository = QuestionsRepository(mockContext)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown(){
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `getAvailableLectureIds returns sorted list of valid lecture IDs`() = testScope.runTest{
        val expectedFiles = arrayOf(
            "lecture_1",
            "Lecture_2",
            "lecture_03",
            "lecture",
            "some_other_files.txt",
            "lecture_abc",
            "lecture123",
            "lecture_5"
        )
        every { mockAssetManager.list("questions") } returns expectedFiles

        val result = repository.getAvailableLectureIds()

        assertEquals(listOf(1, 3, 5), result)
    }

    @Test
    fun `getQuestionsForLecture returns empty list`() = testScope.runTest {
        every { mockAssetManager.list("questions") } returns emptyArray()

        val result = repository.getAvailableLectureIds()

        assertEquals(emptyList<Int>(), result)
    }

    @Test
    fun `getQuestionsForLecture returns parsed questions`() = testScope.runTest {
        val lectureId: Int = 1
        val jsonString = """
            [
                {
                    "id": 1,
                    "questionText": "What is program?",
                    "options": [
                      "A program is a set of precise instructions written in a programming language that tell a computer how to perform a specific task",
                      "A program is a piece of hardware, like a graphics card or a processor, that physically makes a computer work",
                      "Data type",
                      "A program is a general idea or a plan for solving a problem, but it doesn't need to be written down for a computer to understand"
                    ],
                    "correctAnswers": [0],
                    "questionType": "SINGLE_CHOICE"
                }
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(jsonString.toByteArray())

        every { mockAssetManager.open("questions/lecture_${lectureId}/questions.json") } returns inputStream

        val result = repository.getQuestionsForLecture(lectureId)

        assertNotNull(result)
        assertEquals("1", result[0].id)
        assertEquals("What is program?", result[0].question)
        assertEquals(1, result.size)
        assertEquals(listOf("0"), result[0].correctAnswers)
        assertEquals(QuestionType.SINGLE_CHOICE, result[0].questionType)
    }

    @Test
    fun `getQuestionsForLecture returns empty list when lecture_id is negative`() = testScope.runTest {
        val lecture_id = "-1"

        every { mockAssetManager.open("questions/lecture_${lecture_id}/questions.json")
        } throws FileNotFoundException()

        val result = repository.getQuestionsForLecture(lecture_id.toInt())

        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun `getQuestionsForLecture returns empty list for invalid json`() = testScope.runTest {
        val lecture_id = "1"
        val invalidJson = "[]"
        val inputStream = ByteArrayInputStream(invalidJson.toByteArray())

        every { mockAssetManager.open("questions/lecture_${lecture_id}/questions.json")
        } returns inputStream

        val result = repository.getQuestionsForLecture(lecture_id.toInt())

        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun `parseQuestionsJson returns null for empty json` () {
        // using reflected testing
        val method = QuestionsRepository::class.java.getDeclaredMethod("parseQuestionsJson",
            String::class.java)
        method.isAccessible = true

        val result = method.invoke(repository, "")

        assertTrue(result == null)
    }

    @Test
    fun `parseQuestionsJson returns parsed json file`(){
        val jsonString = """
            [
                {
                    "id": 1,
                    "questionText": "What is program?",
                    "options": [
                      "A program is a set of precise instructions written in a programming language that tell a computer how to perform a specific task",
                      "A program is a piece of hardware, like a graphics card or a processor, that physically makes a computer work",
                      "Data type",
                      "A program is a general idea or a plan for solving a problem, but it doesn't need to be written down for a computer to understand"
                    ],
                    "correctAnswers": [0],
                    "questionType": "SINGLE_CHOICE"
                }
            ]
        """.trimIndent()
        val method = QuestionsRepository::class.java.getDeclaredMethod("parseQuestionsJson",
            String::class.java)
        method.isAccessible = true

        val result = method.invoke(repository, jsonString) as? List<QuizQuestion>?

        assertEquals(QuizQuestion(
            id = "1",
            question = "What is program?",
            options = listOf<String>("A program is a set of precise instructions written in a programming language that tell a computer how to perform a specific task",
                "A program is a piece of hardware, like a graphics card or a processor, that physically makes a computer work",
                "Data type",
                "A program is a general idea or a plan for solving a problem, but it doesn't need to be written down for a computer to understand"
            ),
            correctAnswers = listOf<String>("0"),
            questionType = QuestionType.SINGLE_CHOICE
        ), result?.get(0))
    }

    @Test
    fun `parseQuestionsJson returns null for wrong values in json` () {
        // using reflected testing
        val method = QuestionsRepository::class.java.getDeclaredMethod("parseQuestionsJson",
            String::class.java)
        method.isAccessible = true

        val result = method.invoke(repository, "asdd")

        assertTrue(result == null)
    }

    @Test
    fun `parseQuestionsJson returns parsed json for inputChoice`() {
        val jsonString = """
        [
            {
                "id": 8,
                "questionText": "Write the command to print an integer variable named age.",
                "options": [],
                "correctAnswers": ["printf(\"%d\", age);", "printf(\"%d\n\", age);"],
                "questionType": "INPUT_CHOICE"
            }
        ]
        """.trimIndent()

        val method = QuestionsRepository::class.java.getDeclaredMethod(
            "parseQuestionsJson",
            String::class.java
        )
        method.isAccessible = true

        val result = method.invoke(repository, jsonString) as? List<QuizQuestion>?

        // Используем реальный символ новой строки
        val expectedQuestion = QuizQuestion(
            id = "8",
            question = "Write the command to print an integer variable named age.",
            options = emptyList(),
            correctAnswers = listOf(
                "printf(\"%d\", age);",
                "printf(\"%d\n\", age);"
            ),
            questionType = QuestionType.INPUT_CHOICE
        )

        assertEquals(expectedQuestion, result?.get(0))
    }

    @Test
    fun `parseCorrectAnswers returns answer for inputChoice`(){
        val jsonString = """
        [
            {
                "id": 8,
                "questionText": "Write the command to print an integer variable named age.",
                "options": [],
                "correctAnswers": ["printf(\"%d\", age);", "printf(\"%d\n\", age);"],
                "questionType": "INPUT_CHOICE"
            }
        ]
        """.trimIndent()

        val method = QuestionsRepository::class.java.getDeclaredMethod(
            "parseQuestionsJson",
            String::class.java
        )
        method.isAccessible = true

        val result = method.invoke(repository, jsonString) as? List<QuizQuestion>?

        assertEquals(listOf(
            "printf(\"%d\", age);",
            "printf(\"%d\n\", age);"), result?.get(0)?.correctAnswers)
    }
}