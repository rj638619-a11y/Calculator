## 2025-05-20 - Compose Button Accessibility Semantics
**Learning:** Custom Compose buttons (like `LiquidGlassButton`) rendering text symbols (`÷`, `×`, `−`, `⌫`, `=`) are uninformative for screen readers (e.g. TalkBack) unless explicit content descriptions are set via `Modifier.semantics { this.contentDescription = ... }`.
**Action:** Always provide optional `contentDescription` parameter on reusable Compose button components and pass clear spoken labels for mathematical symbols and icon/function buttons.
