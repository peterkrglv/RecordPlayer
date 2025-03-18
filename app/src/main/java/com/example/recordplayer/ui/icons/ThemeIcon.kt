package com.example.recordplayer.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ThemeIcon: ImageVector
    get() {
        if (_ThemeIcon != null) {
            return _ThemeIcon!!
        }
        _ThemeIcon = ImageVector.Builder(
            name = "ThemeIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveTo(396f, 564f)
                quadToRelative(-32f, -32f, -58.5f, -67f)
                reflectiveQuadTo(289f, 423f)
                quadToRelative(-5f, 14f, -6.5f, 28.5f)
                reflectiveQuadTo(281f, 480f)
                quadToRelative(0f, 83f, 58f, 141f)
                reflectiveQuadToRelative(141f, 58f)
                quadToRelative(14f, 0f, 28.5f, -2f)
                reflectiveQuadToRelative(28.5f, -6f)
                quadToRelative(-39f, -22f, -74f, -48.5f)
                reflectiveQuadTo(396f, 564f)
                close()
                moveTo(453f, 508f)
                quadToRelative(51f, 51f, 114f, 87.5f)
                reflectiveQuadTo(702f, 652f)
                quadToRelative(-40f, 51f, -98f, 79.5f)
                reflectiveQuadTo(481f, 760f)
                quadToRelative(-117f, 0f, -198.5f, -81.5f)
                reflectiveQuadTo(201f, 480f)
                quadToRelative(0f, -65f, 28.5f, -123f)
                reflectiveQuadToRelative(79.5f, -98f)
                quadToRelative(20f, 72f, 56.5f, 135f)
                reflectiveQuadTo(453f, 508f)
                close()
                moveTo(743f, 580f)
                quadToRelative(-20f, -5f, -39.5f, -11f)
                reflectiveQuadTo(665f, 555f)
                quadToRelative(8f, -18f, 11.5f, -36.5f)
                reflectiveQuadTo(680f, 480f)
                quadToRelative(0f, -83f, -58.5f, -141.5f)
                reflectiveQuadTo(480f, 280f)
                quadToRelative(-20f, 0f, -38.5f, 3.5f)
                reflectiveQuadTo(405f, 295f)
                quadToRelative(-8f, -19f, -13.5f, -38f)
                reflectiveQuadTo(381f, 218f)
                quadToRelative(24f, -9f, 49f, -13.5f)
                reflectiveQuadToRelative(51f, -4.5f)
                quadToRelative(117f, 0f, 198.5f, 81.5f)
                reflectiveQuadTo(761f, 480f)
                quadToRelative(0f, 26f, -4.5f, 51f)
                reflectiveQuadTo(743f, 580f)
                close()
                moveTo(440f, 120f)
                verticalLineToRelative(-120f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(120f)
                horizontalLineToRelative(-80f)
                close()
                moveTo(440f, 960f)
                verticalLineToRelative(-120f)
                horizontalLineToRelative(80f)
                lineTo(520f, 960f)
                horizontalLineToRelative(-80f)
                close()
                moveTo(763f, 254f)
                lineTo(706f, 197f)
                lineTo(791f, 113f)
                lineTo(848f, 169f)
                lineTo(763f, 254f)
                close()
                moveTo(169f, 847f)
                lineToRelative(-57f, -56f)
                lineToRelative(85f, -85f)
                lineToRelative(57f, 57f)
                lineToRelative(-85f, 84f)
                close()
                moveTo(840f, 520f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(120f)
                verticalLineToRelative(80f)
                lineTo(840f, 520f)
                close()
                moveTo(0f, 520f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(120f)
                verticalLineToRelative(80f)
                lineTo(0f, 520f)
                close()
                moveTo(791f, 848f)
                lineTo(706f, 763f)
                lineTo(763f, 706f)
                lineTo(847f, 791f)
                lineTo(791f, 848f)
                close()
                moveTo(197f, 254f)
                lineToRelative(-84f, -85f)
                lineToRelative(56f, -57f)
                lineToRelative(85f, 85f)
                lineToRelative(-57f, 57f)
                close()
                moveTo(396f, 564f)
                close()
            }
        }.build()

        return _ThemeIcon!!
    }

@Suppress("ObjectPropertyName")
private var _ThemeIcon: ImageVector? = null
