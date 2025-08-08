package org.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.clickable

// PUBLIC_INTERFACE
class MainActivity : ComponentActivity() {
    /**
     * The entry point of the Notes App activity.
     * Sets the Compose UI to the notes list, handles navigation to edit/create screens.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppTheme {
                NotesMainScreen()
            }
        }
    }
}

// Color definitions as per requirements
val PrimaryColor = Color(0xFF1976D2)
val AccentColor = Color(0xFFFFC107)
val SecondaryColor = Color(0xFF424242)
val OnPrimary = Color.White
val BackgroundColor = Color.White
val SurfaceColor = Color(0xFFF6F6F6)

@Composable
fun NotesMainScreen(notesViewModel: NotesViewModel = viewModel()) {
    var screenState by remember { mutableStateOf<ScreenState>(ScreenState.NoteList) }
    var editingNoteId by remember { mutableStateOf<String?>(null) }

    val handleEdit: (String) -> Unit = { id ->
        editingNoteId = id
        screenState = ScreenState.NoteEdit
    }
    val handleCreate: () -> Unit = {
        editingNoteId = null
        screenState = ScreenState.NoteEdit
    }
    val handleBack: () -> Unit = {
        screenState = ScreenState.NoteList
        editingNoteId = null
    }

    Surface(color = BackgroundColor, modifier = Modifier.fillMaxSize()) {
        when (screenState) {
            ScreenState.NoteList -> {
                NotesListScreen(
                    notes = notesViewModel.searchResults,
                    onSearch = notesViewModel::searchNotes,
                    onEditNote = handleEdit,
                    onDeleteNote = notesViewModel::deleteNote,
                    onAddNote = handleCreate
                )
            }
            ScreenState.NoteEdit -> {
                NoteEditScreen(
                    note = editingNoteId?.let { notesViewModel.getNoteById(it) },
                    onSave = { note ->
                        if (note.id == null) {
                            notesViewModel.createNote(note.title, note.content)
                        } else {
                            notesViewModel.updateNote(note)
                        }
                        handleBack()
                    },
                    onDelete = {
                        if (editingNoteId != null) {
                            notesViewModel.deleteNote(editingNoteId!!)
                        }
                        handleBack()
                    },
                    onCancel = handleBack
                )
            }
        }
    }
}

enum class ScreenState { NoteList, NoteEdit }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    notes: List<Note>,
    onSearch: (String) -> Unit,
    onEditNote: (String) -> Unit,
    onDeleteNote: (String) -> Unit,
    onAddNote: () -> Unit
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Notes", color = OnPrimary)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryColor)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNote,
                containerColor = AccentColor,
                contentColor = Color.Black
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Note")
            }
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundColor)
        ) {
            // Search bar
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceColor)
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Search, contentDescription = "Search", tint = SecondaryColor)
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        onSearch(it.text)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (searchText.text.isEmpty()) {
                            Text(
                                "Search notes...",
                                color = SecondaryColor.copy(alpha = 0.5f)
                            )
                        }
                        innerTextField()
                    }
                )
            }

            // Notes list
            if (notes.isEmpty()) {
                Box(Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No notes found", color = SecondaryColor.copy(alpha = 0.4f))
                }
            } else {
                androidx.compose.foundation.lazy.LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(bottom = 72.dp)
                ) {
                    items(notes.size) { idx ->
                        val note = notes[idx]
                        NoteListItem(
                            note = note,
                            onEdit = { onEditNote(note.id!!) },
                            onDelete = { onDeleteNote(note.id!!) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteListItem(note: Note, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(
        color = SurfaceColor,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(1f)
                    .clickable { onEdit() }
            ) {
                Text(
                    note.title,
                    color = PrimaryColor,
                    style = MaterialTheme.typography.titleMedium
                )
                if (note.content.trim().isNotEmpty()) {
                    Text(
                        note.content,
                        color = SecondaryColor,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2
                    )
                }
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = PrimaryColor)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = AccentColor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    note: Note?,
    onSave: (Note) -> Unit,
    onDelete: (() -> Unit)? = null,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }

    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = OnPrimary)
                    }
                },
                title = {
                    Text(if (note == null) "New Note" else "Edit Note", color = OnPrimary)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryColor),
                actions = {
                    if (note != null && onDelete != null) {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = AccentColor)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    focusManager.clearFocus(force = true)
                    onSave(
                        Note(
                            id = note?.id,
                            title = title.trim().ifEmpty { "Untitled" },
                            content = content.trim()
                        )
                    )
                },
                containerColor = AccentColor,
                contentColor = Color.Black
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Save Note")
            }
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(18.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = SecondaryColor,
                    cursorColor = PrimaryColor
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(18.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                minLines = 7,
                maxLines = 16,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = SecondaryColor,
                    cursorColor = PrimaryColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

// --- DOMAIN MODEL AND STORAGE ---

// PUBLIC_INTERFACE
data class Note(
    val id: String? = null,
    val title: String,
    val content: String
)

// PUBLIC_INTERFACE
class NotesRepository {
    // In-memory mock storage, replace with local DB as needed.
    private var notes = mutableStateListOf<Note>()
    private var idCounter = 1

    fun getNotes(): List<Note> = notes

    fun getNoteById(id: String): Note? = notes.find { it.id == id }

    fun searchNotes(query: String): List<Note> =
        if (query.isBlank()) notes
        else notes.filter { it.title.contains(query, true) || it.content.contains(query, true) }

    fun createNote(title: String, content: String): Note {
        val note = Note(id = (idCounter++).toString(), title = title, content = content)
        notes.add(0, note)
        return note
    }

    fun updateNote(note: Note) {
        notes.replaceAll { if (it.id == note.id) note else it }
    }

    fun deleteNote(id: String) {
        notes.removeAll { it.id == id }
    }
}

// PUBLIC_INTERFACE
class NotesViewModel : androidx.lifecycle.ViewModel() {
    private val repository = NotesRepository()

    private val _searchResults = mutableStateListOf<Note>()
    val searchResults: List<Note> get() = _searchResults

    init {
        // Optionally, populate with mock notes.
        repository.createNote("Welcome", "This is your first note!")
        repository.createNote("Try editing me", "Tap the pencil icon to edit.")
        updateSearch("")
    }

    fun getNoteById(id: String): Note? = repository.getNoteById(id)

    fun createNote(title: String, content: String) {
        repository.createNote(title, content)
        updateSearch("")
    }

    fun updateNote(note: Note) {
        repository.updateNote(note)
        updateSearch("")
    }

    fun deleteNote(id: String) {
        repository.deleteNote(id)
        updateSearch("")
    }

    fun searchNotes(query: String) = updateSearch(query)

    private fun updateSearch(query: String) {
        _searchResults.clear()
        _searchResults.addAll(repository.searchNotes(query))
    }
}

// --- THEME ---

@Composable
fun NotesAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = PrimaryColor,
            secondary = SecondaryColor,
            background = BackgroundColor,
            surface = SurfaceColor,
            onPrimary = OnPrimary,
            onSecondary = OnPrimary
        ),
        typography = Typography(),
        content = content
    )
}
