# Biscuit User Guide

![Biscuit UI Screenshot](Ui.png)

Biscuit is a lightweight task-tracking chatbot with a **GUI** (and CLI support) that helps you create, view, and manage tasks. Tasks are **saved automatically** and loaded again when you restart the app.

---

## Quick start

### Run (development)
```bash
./gradlew run

## Display Commands

// Describe the action and its outcome.

Displays the different types of commands that Biscuit can accept, along with how to format the command.

// Give examples of usage

Example: `display` or 'help'

// A description of the expected outcome goes here
Available commands:
  list
  todo <description>
  deadline <description> /by YYYY-MM-DD
  event <description> /from YYYY-MM-DD HH:mm /to YYYY-MM-DD HH:mm
  within <description> /from YYYY-MM-DD /to YYYY-MM-DD
  mark <taskNumber>
  unmark <taskNumber>
  delete <taskNumber>
  find <keyword>
  display   (or: help)
  bye


```
expected output
```

## Todo

// Add a todo task


## Mark

// Mark a task as done
