package org.springframework.fu.kofu.poc

interface Semigroup<F> {
    fun F.combine(other: F): F
}

