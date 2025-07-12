package co.malvinr.quiz_compose.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class QuizRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : QuizRepository {
    override fun getQuizResources(): Flow<List<QuizEntity>> = flow {
        val perguntasPorCategoriaENivel = lerPerguntasDoJsonCompleto()
        val perguntas = perguntasPorCategoriaENivel["Loops de Repetição"]?.get("Fácil") ?: emptyList()
        val quizEntities = perguntas.map { pergunta ->
            val answers = buildList {
                add(AnswerEntity(pergunta.resposta, isCorrect = true))
                addAll(pergunta.opcoes.filter { it != pergunta.resposta }.map { AnswerEntity(it, isCorrect = false) })
            }
            QuizEntity(pergunta.pergunta, answers.shuffled())
        }
        emit(quizEntities)
    }.catch {
        emit(emptyList())
    }.flowOn(Dispatchers.IO)

    private fun lerPerguntasDoJsonCompleto(): QuestionsJson {
        return try {
            val assetManager = context.assets
            val inputStream = assetManager.open("questions.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            val type = object : com.google.gson.reflect.TypeToken<QuestionsJson>() {}.type
            com.google.gson.Gson().fromJson(json, type)
        } catch (e: Exception) {
            emptyMap()
        }
    }
}