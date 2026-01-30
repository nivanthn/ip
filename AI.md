# AI Tooling Log (AI.md)

This file records how AI tools were used during this project, what was changed, and what we learned.

---

## 2026-01-26 — Java code style standardization (SE-EDU Intermediate)

**Tool used:** ChatGPT

**Goal / Increment:**  
Bring all Java source files in the project in line with the SE-EDU Java coding conventions (basic + intermediate).  
Reference: https://se-education.org/guides/conventions/java/intermediate.html

**What the AI did (high level):**
- Standardized formatting to match conventions (indentation, braces, spacing, line wrapping).
- Made naming more consistent (e.g., collection variable names plural and descriptive).
- Ensured public APIs have clear, minimal Javadoc (especially for public classes/methods).
- Reduced unnecessary visibility where applicable (helper methods → `private`).
- Ensured consistency across files so the project reads like it was written by one person.

**What worked well:**
- Fast detection of repeated style issues (imports, whitespace, naming).
- Helpful at applying consistent conventions across multiple files in one sweep.
- Good at proposing safe refactors that don’t change behavior (renames, visibility tightening).

**What didn’t / required care:**
- It tried to create a package but I am not doing that in this project yet

**Estimated time saved:**
- Manual cleanup estimate: ~1–2 hours.

## 2026-01-30 — JavaDoc Generation

**Tool used:** ChatGPT

**Goal / Increment:**  
Generate JavaDoc comments for most of the methods

**What worked well:**
- Fast detection of what the methods do and summarized it
- Helpful at applying consistent conventions across multiple files.
- Good at proposing changes to current comments

**What didn’t / required care:**
- Some of the comments were incorrect so had to manually check through and change it

**Estimated time saved:**
- Manual cleanup estimate: ~1–2 hours.
