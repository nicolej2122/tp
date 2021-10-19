package seedu.intern.model;

import static java.util.Objects.requireNonNull;
import static seedu.intern.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.intern.commons.core.GuiSettings;
import seedu.intern.commons.core.LogsCenter;
import seedu.intern.model.applicant.Applicant;

/**
 * Represents the in-memory model of the intern book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private final InternWatcher internWatcher;
    private final UndoManager undoManager;
    private final UserPrefs userPrefs;
    private final FilteredList<Applicant> filteredApplicants;
    private Applicant applicant;

    /**
     * Initializes a ModelManager with the given internWatcher and userPrefs.
     */
    public ModelManager(ReadOnlyInternWatcher internWatcher, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(internWatcher, userPrefs);

        logger.fine("Initializing with intern book: " + internWatcher + " and user prefs " + userPrefs);

        this.internWatcher = new InternWatcher(internWatcher);
        this.undoManager = new UndoManager(this.internWatcher);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredApplicants = new FilteredList<>(this.internWatcher.getPersonList());
    }

    public ModelManager() {
        this(new InternWatcher(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getInternWatcherFilePath() {
        return userPrefs.getInternWatcherFilePath();
    }

    @Override
    public void setInternWatcherFilePath(Path internWatcherFilePath) {
        requireNonNull(internWatcherFilePath);
        userPrefs.setInternWatcherFilePath(internWatcherFilePath);
    }

    //=========== InternWatcher ================================================================================

    @Override
    public void setInternWatcher(ReadOnlyInternWatcher internWatcher) {
        this.internWatcher.resetData(internWatcher);
    }

    @Override
    public ReadOnlyInternWatcher getInternWatcher() {
        return internWatcher;
    }

    @Override
    public boolean hasApplicant(Applicant applicant) {
        requireNonNull(applicant);
        return internWatcher.hasPerson(applicant);
    }

    @Override
    public void displayApplicant(Applicant applicant) {
        updateApplicant(applicant);
    }

    @Override
    public void deleteApplicant(Applicant target) {
        internWatcher.removePerson(target);
    }

    @Override
    public void addApplicant(Applicant applicant) {
        internWatcher.addPerson(applicant);
        updateFilteredApplicantList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setApplicant(Applicant target, Applicant editedApplicant) {
        requireAllNonNull(target, editedApplicant);

        internWatcher.setPerson(target, editedApplicant);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedInternWatcher}
     */
    @Override
    public ObservableList<Applicant> getFilteredPersonList() {
        return filteredApplicants;
    }

    @Override
    public Applicant getApplicant() {
        return applicant;
    }

    @Override
    public void updateApplicant(Applicant newApplicant) {
        applicant = newApplicant;
    }

    @Override
    public void updateFilteredApplicantList(Predicate<Applicant> predicate) {
        requireNonNull(predicate);
        filteredApplicants.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return internWatcher.equals(other.internWatcher)
                && userPrefs.equals(other.userPrefs)
                && filteredApplicants.equals(other.filteredApplicants);
    }

    //=========== Undo/Redo ============================================================================

    @Override
    public void commitInternWatcher() {
        undoManager.commitState();
    }

    @Override
    public void undoInternWatcher() {
        undoManager.undo();
    }

    @Override
    public void redoInternWatcher() {
        undoManager.redo();
    }

    @Override
    public boolean isUndoAvailable() {
        return undoManager.canUndo();
    }

    @Override
    public boolean isRedoAvailable() {
        return undoManager.canRedo();
    }

}
