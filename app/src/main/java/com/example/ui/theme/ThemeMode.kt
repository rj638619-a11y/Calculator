package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class ThemeMode(
    val title: String,
    val description: String,
    val primaryAccent: Color,
    val secondaryAccent: Color,
    val tertiaryAccent: Color,
    val surfaceGlass: Color,
    val surfaceGlassLight: Color,
    val borderGlass: Color,
    val backgroundColors: List<Color>,
    val buttonNumberColor: Color,
    val buttonOpColor: Color,
    val buttonFuncColor: Color,
    val buttonActionColor: Color,
    val textPrimary: Color,
    val textSecondary: Color
) {
    AURORA(
        title = "Aurora",
        description = "Northern lights emerald, cyan & deep purple",
        primaryAccent = Color(0xFF00F5D4),
        secondaryAccent = Color(0xFF7B2CBF),
        tertiaryAccent = Color(0xFF06D6A0),
        surfaceGlass = Color(0x33101E2E),
        surfaceGlassLight = Color(0x4D1B3A4B),
        borderGlass = Color(0x5500F5D4),
        backgroundColors = listOf(
            Color(0xFF05131E),
            Color(0xFF0A2540),
            Color(0xFF1E1035),
            Color(0xFF042B28)
        ),
        buttonNumberColor = Color(0x2E1E3A5F),
        buttonOpColor = Color(0x4000F5D4),
        buttonFuncColor = Color(0x337B2CBF),
        buttonActionColor = Color(0x4DFF5C8A),
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFFB0C4DE)
    ),

    OCEAN(
        title = "Ocean",
        description = "Deep abyss sapphire, azure & electric aqua",
        primaryAccent = Color(0xFF38BDF8),
        secondaryAccent = Color(0xFF0284C7),
        tertiaryAccent = Color(0xFF0EA5E9),
        surfaceGlass = Color(0x330B1E38),
        surfaceGlassLight = Color(0x4D13335C),
        borderGlass = Color(0x5538BDF8),
        backgroundColors = listOf(
            Color(0xFF030E1F),
            Color(0xFF071E3D),
            Color(0xFF0F3A66),
            Color(0xFF022044)
        ),
        buttonNumberColor = Color(0x2E132D4E),
        buttonOpColor = Color(0x4038BDF8),
        buttonFuncColor = Color(0x330284C7),
        buttonActionColor = Color(0x4DF43F5E),
        textPrimary = Color(0xFFF0F9FF),
        textSecondary = Color(0xFF93C5FD)
    ),

    SUNSET(
        title = "Sunset",
        description = "Warm twilight violet, coral rose & amber glow",
        primaryAccent = Color(0xFFFF7E67),
        secondaryAccent = Color(0xFFFFB703),
        tertiaryAccent = Color(0xFFFB7185),
        surfaceGlass = Color(0x332E112D),
        surfaceGlassLight = Color(0x4D4A154B),
        borderGlass = Color(0x55FF7E67),
        backgroundColors = listOf(
            Color(0xFF1A0A20),
            Color(0xFF2E0836),
            Color(0xFF4A1235),
            Color(0xFF2B092B)
        ),
        buttonNumberColor = Color(0x2E3F1A44),
        buttonOpColor = Color(0x40FF7E67),
        buttonFuncColor = Color(0x33FB7185),
        buttonActionColor = Color(0x4DF43F5E),
        textPrimary = Color(0xFFFFF1F2),
        textSecondary = Color(0xFFFDA4AF)
    ),

    MIDNIGHT(
        title = "Midnight",
        description = "Obsidian slate, electric indigo & cyber chrome",
        primaryAccent = Color(0xFF818CF8),
        secondaryAccent = Color(0xFF6366F1),
        tertiaryAccent = Color(0xFFA5B4FC),
        surfaceGlass = Color(0x330F172A),
        surfaceGlassLight = Color(0x4D1E293B),
        borderGlass = Color(0x55818CF8),
        backgroundColors = listOf(
            Color(0xFF030712),
            Color(0xFF0F172A),
            Color(0xFF1E1B4B),
            Color(0xFF090D16)
        ),
        buttonNumberColor = Color(0x2E1E293B),
        buttonOpColor = Color(0x40818CF8),
        buttonFuncColor = Color(0x336366F1),
        buttonActionColor = Color(0x4DE11D48),
        textPrimary = Color(0xFFF8FAFC),
        textSecondary = Color(0xFF94A3B8)
    );

    fun getGlassBorderBrush(): Brush {
        return Brush.linearGradient(
            colors = listOf(
                borderGlass.copy(alpha = 0.65f),
                borderGlass.copy(alpha = 0.15f),
                borderGlass.copy(alpha = 0.45f)
            )
        )
    }

    fun getActionGradient(): Brush {
        return Brush.horizontalGradient(
            colors = listOf(primaryAccent, secondaryAccent)
        )
    }
}
