import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import org.junit.jupiter.api.Test

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
        println("--- 失敗パターン ---")
        // → andThenではエラー情報をすべて集約することは難しそう
    }
}