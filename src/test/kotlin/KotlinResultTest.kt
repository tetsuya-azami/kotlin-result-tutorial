import com.github.michaelbull.result.*
import org.junit.jupiter.api.Test

// https://blog.nnn.dev/entry/2023/06/22/110000
class KotlinResultTest {
    @Test
    fun andThen() {
        // 成功したら次のandThenの処理を行う
        // 失敗したらそのまま失敗を返す
        val pureResult = Ok("Succeeded at first")
        println("pure result: $pureResult")  // pure result: Ok(Succeeded at first)

        println("--- 成功パターン ---")
        val andThenResult = pureResult.andThen { it -> Ok("$it and succeeded second") }
        println("andThen result: $andThenResult") // andThen result: Ok(Succeeded at first and succeeded second)
        println("--- 成功パターン ---")

        println()
        println("--- 失敗パターン ---")
        val failureResult = pureResult.andThen { it -> Err("$it and failure") }
        println("failure: $failureResult") // failure: Err(Succeeded at first and failure) → 一個目の失敗までは出力される
        val failureAndThenSuccessResult = failureResult.andThen { it -> Ok("$it and succeeded third") }
        println("failure andThen success: $failureAndThenSuccessResult") // Err(Succeeded at first and failure) → 追加のandThenは何も処理されない
        val failureAndThenFailureResult = failureResult.andThen { it -> Err("$it and failure third") }
        println("failure andThen failure: $failureAndThenFailureResult") // Err(Succeeded at first and failure) → 追加のandThenは何も処理されない
        val orElseResult = failureResult.orElse { Ok("$it orElse") }
        println("orElse: $orElseResult")
        println("--- 失敗パターン ---")
        // → andThenではエラー情報をすべて集約することは難しそう
    }

    @Test
    fun toResultOr() {
        println()
        println()
        println()
        println("findUser(1): ${findUser(1)}")
        println("findUser(5): ${findUser(5)}")
    }

    data class User(val id: Int, val name: String, val status: PaymentStatus) {
        enum class PaymentStatus {
            PAID_USER,
            FREE_USER;
        }
    }

    fun findUser(userId: Int): Result<User, String>{
        val userList = listOf(
            User(id = 1, name = "Ally", status = User.PaymentStatus.FREE_USER),
            User(id = 2, name = "Bob", status = User.PaymentStatus.PAID_USER),
            User(id = 3, name = "Charles", status = User.PaymentStatus.FREE_USER),
            User(id = 4, name = "Daniel", status = User.PaymentStatus.PAID_USER),
        )
        return userList.find { it.id == userId }
            .toResultOr { "User is not found." }
    }
}