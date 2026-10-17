# Uni Course Manager

Uni Course Manager is a simple Android application developed with Kotlin and Jetpack Compose to help university students organize their courses and assignments.

## Features

- **Manage courses:** Add, view, and delete university courses.
- **Manage assignments:** Create assignments linked to specific courses.
- **Track completion:** Mark assignments as completed or incomplete.
- **Set due dates:** Enter assignment deadlines.
- **Local database:** Store courses and assignments locally using Room.
- **Automatic cleanup:** Assignments associated with a deleted course are also deleted.

## Technologies Used

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- Room Database
- SQLite
- Kotlin Coroutines and Flow
- Gradle

## Requirements

- Android Studio
- Android SDK
- An Android emulator or a physical Android device

## Getting Started

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Allow Gradle to sync and download the required dependencies.
4. Run the application on an emulator or connected Android device.

## Database

The application uses a local Room database backed by SQLite to store course and assignment information. Assignments are linked to courses, and deleting a course also deletes its associated assignments.

## Purpose

This project was developed as an individual project for the Mobile Application Development course. It demonstrates Android development with Kotlin, Jetpack Compose, and local database integration.

## Author

Computer Science BSc student
