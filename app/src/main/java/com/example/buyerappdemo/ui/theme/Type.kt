package com.example.buyerappdemo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.unit.sp
import com.example.buyerappdemo.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs
)

val PlusJakartaSans = GoogleFont("Plus Jakarta Sans")
val Inter           = GoogleFont("Inter")

val PlusJakartaSansFamily = FontFamily(
    Font(googleFont = PlusJakartaSans, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = PlusJakartaSans, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = PlusJakartaSans, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = PlusJakartaSans, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = PlusJakartaSans, fontProvider = provider, weight = FontWeight.ExtraBold),
)

val InterFamily = FontFamily(
    Font(googleFont = Inter, fontProvider = provider, weight = FontWeight.Light),
    Font(googleFont = Inter, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = Inter, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = Inter, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = Inter, fontProvider = provider, weight = FontWeight.Bold),
)

// Design system rule:
// Plus Jakarta Sans → Display, Headline (editorial voice)
// Inter            → Title, Body, Label (workhorse)

val AtelierTypography = Typography(

    // ── Display — welcome states, splash (Plus Jakarta Sans) ──────────────
    displayLarge = TextStyle(
        fontFamily   = PlusJakartaSansFamily,
        fontWeight   = FontWeight.ExtraBold,
        fontSize     = 57.sp,
        lineHeight   = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily   = PlusJakartaSansFamily,
        fontWeight   = FontWeight.Bold,
        fontSize     = 45.sp,
        lineHeight   = 52.sp,
        letterSpacing = (-0.25).sp
    ),
    displaySmall = TextStyle(
        fontFamily   = PlusJakartaSansFamily,
        fontWeight   = FontWeight.Bold,
        fontSize     = 36.sp,
        lineHeight   = 44.sp,
        letterSpacing = (-0.25).sp
    ),

    // ── Headline — section titles, screen headers (Plus Jakarta Sans) ─────
    headlineLarge = TextStyle(
        fontFamily   = PlusJakartaSansFamily,
        fontWeight   = FontWeight.ExtraBold,
        fontSize     = 32.sp,
        lineHeight   = 40.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily   = PlusJakartaSansFamily,
        fontWeight   = FontWeight.Bold,
        fontSize     = 28.sp,
        lineHeight   = 36.sp,
        letterSpacing = (-0.3).sp
    ),
    headlineSmall = TextStyle(
        fontFamily   = PlusJakartaSansFamily,
        fontWeight   = FontWeight.Bold,
        fontSize     = 24.sp,
        lineHeight   = 32.sp,
        letterSpacing = (-0.3).sp
    ),

    // ── Title — toolbar, list items (Inter) ───────────────────────────────
    titleLarge = TextStyle(
        fontFamily   = InterFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 22.sp,
        lineHeight   = 28.sp,
        letterSpacing = (-0.2).sp
    ),
    titleMedium = TextStyle(
        fontFamily   = InterFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 16.sp,
        lineHeight   = 24.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily   = InterFamily,
        fontWeight   = FontWeight.Medium,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.sp
    ),

    // ── Body — descriptions, paragraphs (Inter) ───────────────────────────
    bodyLarge = TextStyle(
        fontFamily   = InterFamily,
        fontWeight   = FontWeight.Normal,
        fontSize     = 16.sp,
        lineHeight   = 24.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily   = InterFamily,
        fontWeight   = FontWeight.Normal,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily   = InterFamily,
        fontWeight   = FontWeight.Normal,
        fontSize     = 12.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.sp
    ),

    // ── Label — badges, buttons, metadata (Inter) ─────────────────────────
    labelLarge = TextStyle(
        fontFamily   = InterFamily,
        fontWeight   = FontWeight.Bold,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.3.sp
    ),
    labelMedium = TextStyle(
        fontFamily   = InterFamily,
        fontWeight   = FontWeight.Bold,
        fontSize     = 12.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily   = InterFamily,
        fontWeight   = FontWeight.Bold,
        fontSize     = 11.sp,
        lineHeight   = 16.sp,
        letterSpacing = 1.2.sp  // wider tracking for uppercase metadata
    ),
)

