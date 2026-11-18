LUST-Agent ChatScreen Kotlin syntax fix

Replace this file in your repository:
app/src/main/kotlin/ai/lust/ui/ChatScreen.kt

This fixes the malformed quoted string in mockReply() that caused the reported Kotlin compiler errors around lines 84-87.

This is a targeted source fix only. It has not yet been verified by a successful full GitHub Actions build, and later build errors may still need fixing.
