/*
Bullet Continuous Collision Detection and Physics Library
Copyright (c) 2010 Erwin Coumans  http://bulletphysics.org

This software is provided 'as-is', without any express or implied warranty.
In no event will the authors be held liable for any damages arising from the use of this software.
Permission is granted to anyone to use this software for any purpose,
including commercial applications, and to alter it and redistribute it freely,
subject to the following restrictions:

1. The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgment in the product documentation would be appreciated but is not required.
2. Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
3. This notice may not be removed or altered from any source distribution.
*/

package bullet.collision.collisionShapes

import bullet.linearMath.PI2

// for btTriangleInfo m_flags
val TRI_INFO_V0V1_CONVEX = 1
val TRI_INFO_V1V2_CONVEX = 2
val TRI_INFO_V2V0_CONVEX = 4

val TRI_INFO_V0V1_SWAP_NORMALB = 8
val TRI_INFO_V1V2_SWAP_NORMALB = 16
val TRI_INFO_V2V0_SWAP_NORMALB = 32


/** The TriangleInfo structure stores information to adjust collision normals to avoid collisions against internal edges
 *  it can be generated using */
class TriangleInfo {
    var flags = 0
    var edgeV0V1Angle = PI2
    var edgeV1V2Angle = PI2
    var edgeV2V0Angle = PI2
}

typealias TriangleInfoMap = HashMap<Int, TriangleInfo>
val TriangleInfoMap.convexEpsilon; get() =  0f
val TriangleInfoMap.planarEpsilon; get() = 0.0001f
val TriangleInfoMap.equalVertexThreshold; get() = 0.0001f * 0.0001f
val TriangleInfoMap.edgeDistanceThreshold; get() = 0.1f
val TriangleInfoMap.maxEdgeAngleThreshold; get() = 0.0001f * 0.0001f
val TriangleInfoMap.zeroAreaThreshold; get() = PI2
/** The TriangleInfoMap stores edge angle information for some triangles. You can compute this information yourself or
 *  using GenerateInternalEdgeInfo. */
