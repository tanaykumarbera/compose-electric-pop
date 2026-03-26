package com.electricpop.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Shapes
import sv.lib.squircleshape.SquircleShape

val ElectricPopShapes = Shapes(
    extraSmall = SquircleShape(percent = 25),
    small = SquircleShape(percent = 35),
    medium = SquircleShape(percent = 50),
    large = SquircleShape(percent = 70),
    extraLarge = SquircleShape(percent = 85),
)

val PopShapeFull = CircleShape
