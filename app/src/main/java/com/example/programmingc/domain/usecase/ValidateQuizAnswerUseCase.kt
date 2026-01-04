package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.QuestionType
import com.example.programmingc.domain.model.QuizQuestion
import com.example.programmingc.domain.model.ValidationResult
import javax.inject.Inject

class ValidateQuizAnswerUseCase @Inject constructor() {
    operator fun invoke(
        question: QuizQuestion,
        userAnswers: List<String>
    ): ValidationResult{
        return try {
            when(question.questionType){
                QuestionType.SINGLE_CHOICE -> validateSingleChoice(question, userAnswers)
                QuestionType.MULTIPLE_CHOICE -> validateMultipleChoice(question, userAnswers)
                QuestionType.INPUT_CHOICE -> validateInputChoice(question, userAnswers)
            }
        }catch (e: Exception){
            ValidationResult(
                isCorrect = false,
                message = "Wrong answer!"
            )
        }
    }

    private fun validateSingleChoice(
        question: QuizQuestion,
        userAnswers: List<String>
    ): ValidationResult{
        if (userAnswers.size != 1){
            return ValidationResult(
                isCorrect = false,
                message = "You need to choice one answer!"
            )
        }

        val userAnswer = userAnswers.first()
        val isCorrect = question.correctAnswers.contains(userAnswer)

        return ValidationResult(
            isCorrect = isCorrect,
            message = if (isCorrect){
                "Correct!"
            }
            else{
                "Wrong answer!"
            }
        )
    }

    private fun validateMultipleChoice(
        question: QuizQuestion,
        userAnswers: List<String>
    ): ValidationResult{
        if (userAnswers.isEmpty()){
            return ValidationResult(
                isCorrect = false,
                message = "You havent choose answer!"
            )
        }
        val isCorrect = userAnswers.sorted() == question.correctAnswers.sorted()

        return ValidationResult(
            isCorrect = isCorrect,
            message = if(isCorrect){
                "Correct!"
            }
            else{
                "Wrong answer!"
            }
        )
    }

    private fun validateInputChoice(
        question: QuizQuestion,
        userAnswers: List<String>  // ← Измените сигнатуру на List<String>
    ): ValidationResult {
        println("=== DEBUG VALIDATION ===")
        println("User answers list: $userAnswers")

        if (userAnswers.isEmpty()) {
            println("User input is empty")
            return ValidationResult(
                isCorrect = false,
                message = "You haven't chosen answer!"
            )
        }

        // Берем первый элемент (для INPUT_CHOICE должен быть только один)
        val userAnswer = userAnswers.first().trim()
        println("User answer: '$userAnswer'")
        println("Correct answers: ${question.correctAnswers}")

        // Сравниваем с каждым правильным ответом
        val normalizedUserAnswer = normalizeAnswer(userAnswer)
        println("Normalized user answer: '$normalizedUserAnswer'")

        val isCorrect = question.correctAnswers.any { correctAnswer ->
            val normalizedCorrectAnswer = normalizeAnswer(correctAnswer)
            val matches = normalizedUserAnswer == normalizedCorrectAnswer

            println("Comparing: '$normalizedUserAnswer' with '$normalizedCorrectAnswer' -> $matches")
            matches
        }

        println("Final result: $isCorrect")
        println("=== END DEBUG ===")

        return ValidationResult(
            isCorrect = isCorrect,
            message = if (isCorrect) "Correct!" else "Wrong answer!"
        )
    }
    private fun normalizeAnswer(answer: String): String {
        return answer
            .trim()  // Убираем пробелы в начале и конце
            .replace("\\s+".toRegex(), " ")  // Заменяем множественные пробелы на один
            .replace("\\s*,\\s*".toRegex(), ", ")  // Нормализуем пробелы вокруг запятой
            .replace("\\s*\\(\\s*".toRegex(), "(")  // Убираем пробелы после функции
            .replace("\\s*\\)".toRegex(), ")")  // Убираем пробелы перед закрывающей скобкой
            .replace("\\\\\"".toRegex(), "\"")  // Убираем экранирование кавычек если нужно
            .trim()
    }
}