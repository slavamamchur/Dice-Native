package bullet.linearMath

import bullet.EPSILON
import kotlin.math.abs
import kotlin.math.sqrt

/* SVN $Revision$ on $Date$ from http://bullet.googlecode.com*/
const val BULLET_VERSION = 287
const val LARGE_FLOAT = 9.9999998E17f

val PI = kotlin.math.PI.toFloat()
val PI2 = PI * 2
val HALF_PI = PI * 0.5f
const val SIMDSQRT12 = 0.70710677f
/* reciprocal square root */
fun recipSqrt(x: Float) = 1f / sqrt(x)

fun recip(x: Float) = 1f / x

fun fsels(a: Float, b: Float, c: Float) = if (a >= 0) b else c


/** @returns normalized value in range [-SIMD_PI, SIMD_PI]   */
fun normalizeAngle(angleInRadians: Float): Float {
    val angleInRad = angleInRadians % PI2
    return when {
        angleInRad < -PI -> angleInRad + PI2
        angleInRad > PI -> angleInRad - PI2
        else -> angleInRad
    }
}

/** rudimentary class to provide type info  */
open class TypedObject(val objectType: Int)

val Float.fuzzyZero get() = abs(this) < Float.EPSILON

fun equal(a: Float, eps: Float) = a <= eps && a >= -eps