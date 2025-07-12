package co.malvinr.quiz_compose.data

/**
 * Model para representar uma pergunta do JSON local.
 */
data class PerguntaJson(
    val pergunta: String,
    val opcoes: List<String>,
    val resposta: String
)

/**
 * Model para representar a estrutura completa do JSON local.
 * Cada categoria possui níveis, e cada nível possui uma lista de perguntas.
 */
typealias QuestionsJson = Map<String, Map<String, List<PerguntaJson>>>
