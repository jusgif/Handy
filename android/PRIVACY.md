# Android privacy contract

- Do not add a recording or transcript history.
- Do not persist transcript text in preferences, files, databases, notifications, logs, analytics, or crash reports.
- Keep audio buffers in memory when possible. If a temporary file is unavoidable, store it only in app-private cache and delete it in every terminal path (success, error, cancellation, service stop).
- Keep clipboard use as an explicit fallback only; Android clipboard managers and destination apps may retain copied/inserted text outside Handy's control.
- Model files may be persisted locally. They are not recordings or transcripts.
- No network request may contain user audio or dictated text. Model downloads must be clearly identified as model assets and integrity-checked.
- A privacy claim must be verified against the final native engine, error handling, and release build before publishing.
