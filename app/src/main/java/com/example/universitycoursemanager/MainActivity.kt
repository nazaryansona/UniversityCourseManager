
package com.example.universitycoursemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.universitycoursemanager.ui.theme.UniversityCourseManagerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)

        setContent {
            UniversityCourseManagerTheme {
                CourseScreen(
                    courseDao = database.courseDao(),
                    assignmentDao = database.assignmentDao()
                )
            }
        }
    }
}

@Composable
fun CourseScreen(
    courseDao: CourseDao,
    assignmentDao: AssignmentDao
) {
    val courses by courseDao.getAllCourses()
        .collectAsState(initial = emptyList())

    val assignments by assignmentDao.getAllAssignments()
        .collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var instructor by remember { mutableStateOf("") }
    var credits by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }

    var assignmentTitle by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var selectedCourseId by remember { mutableStateOf<Int?>(null) }

    var message by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(12.dp))

                Text(
                    "Uni Course Manager",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    "Your courses, organized.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))

                Spacer(Modifier.height(8.dp))

                Text(
                    "Add a Course",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Course name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = instructor,
                    onValueChange = { instructor = it },
                    label = { Text("Instructor") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = credits,
                    onValueChange = { credits = it },
                    label = { Text("Credits") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = semester,
                    onValueChange = { semester = it },
                    label = { Text("Semester") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Button(
                    onClick = {
                        val creditValue = credits.toIntOrNull()

                        if (
                            name.isBlank() ||
                            instructor.isBlank() ||
                            semester.isBlank() ||
                            creditValue == null ||
                            creditValue <= 0
                        ) {
                            message = "Please complete all course fields."
                        } else {
                            scope.launch {
                                courseDao.insertCourse(
                                    Course(
                                        name = name.trim(),
                                        instructor = instructor.trim(),
                                        credits = creditValue,
                                        semester = semester.trim()
                                    )
                                )

                                name = ""
                                instructor = ""
                                credits = ""
                                semester = ""
                                message = "Course saved!"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Course")
                }

                Text(
                    "📚 My Courses (${courses.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (courses.isEmpty()) {
                item { Text("No courses added yet.") }
            }

            items(courses, key = { "course_${it.id}" }) { course ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    elevation = androidx.compose.material3.CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            course.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Instructor: ${course.instructor}")
                        Text("Credits: ${course.credits}")
                        Text("Semester: ${course.semester}")

                        TextButton(
                            onClick = {
                                scope.launch {
                                    courseDao.deleteCourse(course)
                                }
                            }
                        ) {
                            Text("Delete course")
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(12.dp))

                Text(
                    "📝 My Assignments (${assignments.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                OutlinedTextField(
                    value = assignmentTitle,
                    onValueChange = { assignmentTitle = it },
                    label = { Text("Assignment title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Select a course:")

                if (courses.isEmpty()) {
                    Text("Add a course before creating an assignment.")
                } else {
                    courses.forEach { course ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.RadioButton(
                                selected = selectedCourseId == course.id,
                                onClick = {
                                    selectedCourseId = course.id
                                }
                            )
                            Text(course.name)
                        }
                    }
                }

                Button(
                    onClick = {
                        val courseId = selectedCourseId

                        if (
                            assignmentTitle.isBlank() ||
                            !dueDate.matches(
                                Regex("""\d{4}-\d{2}-\d{2}""")
                            ) ||
                            courseId == null ||
                            courses.none { it.id == courseId }
                        ) {
                            message =
                                "Enter a title, valid date, and select a course."
                        } else {
                            scope.launch {
                                assignmentDao.insertAssignment(
                                    Assignment(
                                        courseId = courseId,
                                        title = assignmentTitle.trim(),
                                        dueDate = dueDate.trim()
                                    )
                                )

                                assignmentTitle = ""
                                dueDate = ""
                                message = "Assignment saved!"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Assignment")
                }

                Text(
                    "My Assignments (${assignments.size})",
                    style = MaterialTheme.typography.titleLarge
                )

                if (message.isNotBlank()) {
                    Text(message)
                }
            }

            if (assignments.isEmpty()) {
                item { Text("No assignments added yet.") }
            }

            items(assignments, key = { "assignment_${it.id}" }) { assignment ->
                val courseName = courses.find {
                    it.id == assignment.courseId
                }?.name ?: "Unknown course"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    elevation = androidx.compose.material3.CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            assignment.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Course: $courseName")
                        Text("Due: ${assignment.dueDate}")

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = assignment.isCompleted,
                                onCheckedChange = { checked ->
                                    scope.launch {
                                        assignmentDao.updateAssignment(
                                            assignment.copy(
                                                isCompleted = checked
                                            )
                                        )
                                    }
                                }
                            )
                            Text(
                                if (assignment.isCompleted) {
                                    "Completed"
                                } else {
                                    "Mark as completed"
                                }
                            )
                        }

                        TextButton(
                            onClick = {
                                scope.launch {
                                    assignmentDao.deleteAssignment(assignment)
                                }
                            }
                        ) {
                            Text("Delete assignment")
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}
