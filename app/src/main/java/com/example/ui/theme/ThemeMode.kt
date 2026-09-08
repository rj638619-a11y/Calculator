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

    ONE_UI_MINT_LIGHT(
        title = "One UI Mint Light",
        description = "Samsung style clean light silver with vivid mint green",
        primaryAccent = Color(0xFF00B080),
        secondaryAccent = Color(0xFF049E75),
        tertiaryAccent = Color(0xFF10B981),
        surfaceGlass = Color(0x33E2E8F0),
        surfaceGlassLight = Color(0x4DF1F5F9),
        borderGlass = Color(0x5500B080),
        backgroundColors = listOf(
            Color(0xFFF8FAFC),
            Color(0xFFF1F5F9),
            Color(0xFFE2E8F0),
            Color(0xFFFFFFFF)
        ),
        buttonNumberColor = Color(0x4DE2E8F0),
        buttonOpColor = Color(0xE600C896),
        buttonFuncColor = Color(0x4DCB5DF0),
        buttonActionColor = Color(0x4DFF5252),
        textPrimary = Color(0xFF0F172A),
        textSecondary = Color(0xFF64748B),
        isLight = true
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

    AMOLED_LIGHT(
        title = "Amoled Light",
        description = "Clean high-contrast minimal light with ice cyan",
        primaryAccent = Color(0xFF0097A7),
        secondaryAccent = Color(0xFF00838F),
        tertiaryAccent = Color(0xFF38BDF8),
        surfaceGlass = Color(0x33E0F2FE),
        surfaceGlassLight = Color(0x4DBAE6FD),
        borderGlass = Color(0x550097A7),
        backgroundColors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFF8FAFC),
            Color(0xFFF1F5F9),
            Color(0xFFFAFAFA)
        ),
        buttonNumberColor = Color(0x4DF1F5F9),
        buttonOpColor = Color(0xE60097A7),
        buttonFuncColor = Color(0x4D00838F),
        buttonActionColor = Color(0x4DFF3366),
        textPrimary = Color(0xFF09090B),
        textSecondary = Color(0xFF52525B),
        isLight = true
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

    AURORA_LIGHT(
        title = "Aurora Light",
        description = "Northern lights bright emerald & cyan on pristine frost",
        primaryAccent = Color(0xFF00C4A7),
        secondaryAccent = Color(0xFF6A1B9A),
        tertiaryAccent = Color(0xFF059669),
        surfaceGlass = Color(0x33E0F2FE),
        surfaceGlassLight = Color(0x4DBAE6FD),
        borderGlass = Color(0x5500C4A7),
        backgroundColors = listOf(
            Color(0xFFF0FDF4),
            Color(0xFFECFDF5),
            Color(0xFFD1FAE5),
            Color(0xFFFFFFFF)
        ),
        buttonNumberColor = Color(0x4DD1FAE5),
        buttonOpColor = Color(0xE600C4A7),
        buttonFuncColor = Color(0x4D7B2CBF),
        buttonActionColor = Color(0x4DFF5C8A),
        textPrimary = Color(0xFF064E3B),
        textSecondary = Color(0xFF047857),
        isLight = true
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

    OCEAN_LIGHT(
        title = "Ocean Light",
        description = "Crisp sky blue & sapphire on clean azure frost",
        primaryAccent = Color(0xFF0284C7),
        secondaryAccent = Color(0xFF0369A1),
        tertiaryAccent = Color(0xFF38BDF8),
        surfaceGlass = Color(0x33E0F2FE),
        surfaceGlassLight = Color(0x4DBAE6FD),
        borderGlass = Color(0x550284C7),
        backgroundColors = listOf(
            Color(0xFFF0F9FF),
            Color(0xFFE0F2FE),
            Color(0xFFBAE6FD),
            Color(0xFFFFFFFF)
        ),
        buttonNumberColor = Color(0x4DBAE6FD),
        buttonOpColor = Color(0xE60284C7),
        buttonFuncColor = Color(0x4D0369A1),
        buttonActionColor = Color(0x4DF43F5E),
        textPrimary = Color(0xFF0C4A6E),
        textSecondary = Color(0xFF0369A1),
        isLight = true
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

    SUNSET_LIGHT(
        title = "Sunset Light",
        description = "Warm rose, amber & lavender on soft pastel cream",
        primaryAccent = Color(0xFFE11D48),
        secondaryAccent = Color(0xFFD97706),
        tertiaryAccent = Color(0xFFBE185D),
        surfaceGlass = Color(0x33FFE4E6),
        surfaceGlassLight = Color(0x4DFEF2F2),
        borderGlass = Color(0x55E11D48),
        backgroundColors = listOf(
            Color(0xFFFFF1F2),
            Color(0xFFFFE4E6),
            Color(0xFFFECDD3),
            Color(0xFFFFFFFF)
        ),
        buttonNumberColor = Color(0x4DFECDD3),
        buttonOpColor = Color(0xE6E11D48),
        buttonFuncColor = Color(0x4D9333EA),
        buttonActionColor = Color(0x4DF43F5E),
        textPrimary = Color(0xFF4C0519),
        textSecondary = Color(0xFF881337),
        isLight = true
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

    CYBER_NEON_LIGHT(
        title = "Cyber Neon Light",
        description = "Bright lime & vibrant teal on clean paper white",
        primaryAccent = Color(0xFF65A30D),
        secondaryAccent = Color(0xFF16A34A),
        tertiaryAccent = Color(0xFF0D9488),
        surfaceGlass = Color(0x33ECFCCB),
        surfaceGlassLight = Color(0x4DFEF08A),
        borderGlass = Color(0x5565A30D),
        backgroundColors = listOf(
            Color(0xFFF7FEE7),
            Color(0xFFECFCCB),
            Color(0xFFD9F99D),
            Color(0xFFFFFFFF)
        ),
        buttonNumberColor = Color(0x4DD9F99D),
        buttonOpColor = Color(0xE665A30D),
        buttonFuncColor = Color(0x4D0D9488),
        buttonActionColor = Color(0x4DFF0055),
        textPrimary = Color(0xFF1A2E05),
        textSecondary = Color(0xFF365314),
        isLight = true
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

    NORDIC_LILAC_LIGHT(
        title = "Nordic Lilac Light",
        description = "Soft lavender, periwinkle & pastel rose on snow white",
        primaryAccent = Color(0xFF7C3AED),
        secondaryAccent = Color(0xFF6D28D9),
        tertiaryAccent = Color(0xFFDB2777),
        surfaceGlass = Color(0x33F3E8FF),
        surfaceGlassLight = Color(0x4DEDE9FE),
        borderGlass = Color(0x557C3AED),
        backgroundColors = listOf(
            Color(0xFFFAF5FF),
            Color(0xFFF3E8FF),
            Color(0xFFE9D5FF),
            Color(0xFFFFFFFF)
        ),
        buttonNumberColor = Color(0x4DE9D5FF),
        buttonOpColor = Color(0xE67C3AED),
        buttonFuncColor = Color(0x4D6D28D9),
        buttonActionColor = Color(0x4DFB7185),
        textPrimary = Color(0xFF2E1065),
        textSecondary = Color(0xFF5B21B6),
        isLight = true
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
    ),

    MIDNIGHT_LIGHT(
        title = "Midnight Light",
        description = "Clean corporate indigo & slate on crisp paper white",
        primaryAccent = Color(0xFF4F46E5),
        secondaryAccent = Color(0xFF4338CA),
        tertiaryAccent = Color(0xFF6366F1),
        surfaceGlass = Color(0x33EEF2F6),
        surfaceGlassLight = Color(0x4DE2E8F0),
        borderGlass = Color(0x554F46E5),
        backgroundColors = listOf(
            Color(0xFFF8FAFC),
            Color(0xFFF1F5F9),
            Color(0xFFE2E8F0),
            Color(0xFFFFFFFF)
        ),
        buttonNumberColor = Color(0x4DE2E8F0),
        buttonOpColor = Color(0xE64F46E5),
        buttonFuncColor = Color(0x4D4338CA),
        buttonActionColor = Color(0x4DE11D48),
        textPrimary = Color(0xFF0F172A),
        textSecondary = Color(0xFF475569),
        isLight = true
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
