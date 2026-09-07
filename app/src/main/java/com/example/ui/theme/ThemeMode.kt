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
    val textSecondary: Color,
    val isLight: Boolean = false
) {
    ONE_UI_MINT(
        title = "One UI Mint",
        description = "Samsung style sleek dark slate with vivid mint green",
        primaryAccent = Color(0xFF00D09C),
        secondaryAccent = Color(0xFF05B98A),
        tertiaryAccent = Color(0xFF64E2BD),
        surfaceGlass = Color(0x33101820),
        surfaceGlassLight = Color(0x4D1A2530),
        borderGlass = Color(0x5500D09C),
        backgroundColors = listOf(
            Color(0xFF0B1015),
            Color(0xFF101922),
            Color(0xFF14222D),
            Color(0xFF0A1218)
        ),
        buttonNumberColor = Color(0x2E1E2B38),
        buttonOpColor = Color(0x3800D09C),
        buttonFuncColor = Color(0x2E2A3C4D),
        buttonActionColor = Color(0x4DFF5252),
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFF90A4AE)
    ),

    IOS_DARK(
        title = "iOS Dark",
        description = "Iconic Apple iOS calculator with vibrant orange & titanium",
        primaryAccent = Color(0xFFFF9F0A),
        secondaryAccent = Color(0xFFFFB340),
        tertiaryAccent = Color(0xFFFFD60A),
        surfaceGlass = Color(0x331C1C1E),
        surfaceGlassLight = Color(0x4D2C2C2E),
        borderGlass = Color(0x55FF9F0A),
        backgroundColors = listOf(
            Color(0xFF000000),
            Color(0xFF0D0D0E),
            Color(0xFF151517),
            Color(0xFF050505)
        ),
        buttonNumberColor = Color(0x4D333333),
        buttonOpColor = Color(0xE6FF9F0A),
        buttonFuncColor = Color(0x4DA5A5A5),
        buttonActionColor = Color(0x4DA5A5A5),
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFFA5A5A5)
    ),

    IOS_LIGHT(
        title = "iOS Light",
        description = "Clean minimalist light aesthetic with vibrant orange",
        primaryAccent = Color(0xFFFF9500),
        secondaryAccent = Color(0xFFFF5E3A),
        tertiaryAccent = Color(0xFFFF851B),
        surfaceGlass = Color(0x33E5E5EA),
        surfaceGlassLight = Color(0x4DF2F2F7),
        borderGlass = Color(0x338E8E93),
        backgroundColors = listOf(
            Color(0xFFF2F2F7),
            Color(0xFFE5E5EA),
            Color(0xFFD1D1D6),
            Color(0xFFF9F9FB)
        ),
        buttonNumberColor = Color(0x66FFFFFF),
        buttonOpColor = Color(0xE6FF9500),
        buttonFuncColor = Color(0x4DD1D1D6),
        buttonActionColor = Color(0x4DFF3B30),
        textPrimary = Color(0xFF000000),
        textSecondary = Color(0xFF6C6C70),
        isLight = true
    ),

    AMOLED_STEALTH(
        title = "AMOLED Stealth",
        description = "Ultra minimalist pitch black with ice cyan highlights",
        primaryAccent = Color(0xFF00F0FF),
        secondaryAccent = Color(0xFF00B4D8),
        tertiaryAccent = Color(0xFF70E000),
        surfaceGlass = Color(0x220A0A0A),
        surfaceGlassLight = Color(0x44141414),
        borderGlass = Color(0x4400F0FF),
        backgroundColors = listOf(
            Color(0xFF000000),
            Color(0xFF030303),
            Color(0xFF080808),
            Color(0xFF010101)
        ),
        buttonNumberColor = Color(0x33181818),
        buttonOpColor = Color(0x3300F0FF),
        buttonFuncColor = Color(0x28262626),
        buttonActionColor = Color(0x4DFF3366),
        textPrimary = Color(0xFFFAFAFA),
        textSecondary = Color(0xFF888888)
    ),

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

    CYBER_NEON(
        title = "Cyber Neon",
        description = "Electric lime & cyber yellow with obsidian matrix",
        primaryAccent = Color(0xFFCCFF00),
        secondaryAccent = Color(0xFF99FF00),
        tertiaryAccent = Color(0xFF00FFA3),
        surfaceGlass = Color(0x33121A0F),
        surfaceGlassLight = Color(0x4D1D2C19),
        borderGlass = Color(0x55CCFF00),
        backgroundColors = listOf(
            Color(0xFF080D06),
            Color(0xFF0F190D),
            Color(0xFF172412),
            Color(0xFF060904)
        ),
        buttonNumberColor = Color(0x2E1C2B18),
        buttonOpColor = Color(0x40CCFF00),
        buttonFuncColor = Color(0x3300FFA3),
        buttonActionColor = Color(0x4DFF0055),
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xFFCCFF88)
    ),

    NORDIC_LILAC(
        title = "Nordic Lilac",
        description = "Pastel lavender, soft periwinkle & dreamy rose",
        primaryAccent = Color(0xFFC4B5FD),
        secondaryAccent = Color(0xFFA78BFA),
        tertiaryAccent = Color(0xFFF472B6),
        surfaceGlass = Color(0x331E1B2E),
        surfaceGlassLight = Color(0x4D2D2745),
        borderGlass = Color(0x55C4B5FD),
        backgroundColors = listOf(
            Color(0xFF110E1C),
            Color(0xFF1B162B),
            Color(0xFF251E3C),
            Color(0xFF130F20)
        ),
        buttonNumberColor = Color(0x2E2C2644),
        buttonOpColor = Color(0x40C4B5FD),
        buttonFuncColor = Color(0x33A78BFA),
        buttonActionColor = Color(0x4DFB7185),
        textPrimary = Color(0xFFFAF5FF),
        textSecondary = Color(0xFFDDD6FE)
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
