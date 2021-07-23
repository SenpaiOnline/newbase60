/*
 * Copyright (c) 2021 Maxim Pavlov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package online.senpai.newbase60

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.longs
import java.text.ParseException

@OptIn(ExperimentalKotest::class)
class NewBase60Tests : FunSpec({
    context("number to newbase60 tests") {
        context("simple cases") {
            withData(
                nameFn = { pair: Pair<Long, String> -> "${pair.first}L to ${pair.second}" },
                0L to "0",
                1L to "1",
                60L to "10",
                120L to "20",
                1337L to "NH",
            ) { (number: Long, sexagesimal: String) ->
                numberToSexagesimal(number).shouldBeEqualComparingTo(sexagesimal)
            }
        }
        context("invalid values") {
            shouldThrowExactly<IllegalArgumentException> { numberToSexagesimal(-1) }
        }
    }

    context("newbase60 to number tests") {
        context("simple cases") {
            withData(
                nameFn = { pair: Pair<String, Long> -> "${pair.first} to ${pair.second}L" },
                "0" to 0L,
                "1" to 1L,
                "10" to 60L,
                "20" to 120L,
                "NH" to 1337L
            ) { (sexagesimal: String, number: Long) ->
                sexagesimalToNumber(sexagesimal).shouldBeEqualComparingTo(number)
            }
        }
        context("invalid values") {
            withData(
                ",",
                "\uD83E\uDD7A",
                "NH\uD83E\uDD7A",
                "\uD83E\uDD7ANH"
            ) { sexagesimal: String ->
                shouldThrowExactly<ParseException> { sexagesimalToNumber(sexagesimal) }
            }
        }
        context("typos correction") {
            withData(
                "l" to 1L,
                "I" to 1L,
                "O" to 0L,
                "-" to 34L
            ) { (sexagesimal: String, number: Long) ->
                sexagesimalToNumber(sexagesimal).shouldBeEqualComparingTo(number)
            }
        }
    }

    context("round trip test") {
        checkAll(Exhaustive.longs(0L..60_000L)) { number: Long ->
            sexagesimalToNumber(numberToSexagesimal(number)).shouldBeEqualComparingTo(number)
        }
    }
})
