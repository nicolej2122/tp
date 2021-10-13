package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GRADE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GRADUATIONYEARMONTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSTITUTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SKILL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Applicant;
import seedu.address.model.person.ApplicationStatus;
import seedu.address.model.person.Course;
import seedu.address.model.person.Email;
import seedu.address.model.person.Grade;
import seedu.address.model.person.GraduationYearMonth;
import seedu.address.model.person.Institution;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.skills.Skill;

/**
 * Edits the details of an existing applicant in Intern Watcher.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the applicant identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_GRADE + "GRADE] "
            + "[" + PREFIX_INSTITUTION + "INSTITUTION] "
            + "[" + PREFIX_COURSE + "COURSE] "
            + "[" + PREFIX_GRADUATIONYEARMONTH + "GRADUATION_YEAR_MONTH] "
            + "[" + PREFIX_STATUS + "STATUS] "
            + "[" + PREFIX_SKILL + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Applicant: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in Intern Watcher.";

    private final Index index;
    private final EditApplicantDescriptor editApplicantDescriptor;

    /**
     * @param index of the applicant in the filtered applicant list to edit
     * @param editApplicantDescriptor details to edit the applicant with
     */
    public EditCommand(Index index, EditApplicantDescriptor editApplicantDescriptor) {
        requireNonNull(index);
        requireNonNull(editApplicantDescriptor);

        this.index = index;
        this.editApplicantDescriptor = new EditApplicantDescriptor(editApplicantDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Applicant> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Applicant applicantToEdit = lastShownList.get(index.getZeroBased());
        Applicant editedApplicant = createEditedPerson(applicantToEdit, editApplicantDescriptor);

        if (!applicantToEdit.isSamePerson(editedApplicant) && model.hasPerson(editedApplicant)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(applicantToEdit, editedApplicant);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedApplicant));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Applicant createEditedPerson(Applicant applicantToEdit,
                                                EditApplicantDescriptor editApplicantDescriptor) {
        assert applicantToEdit != null;

        Name updatedName = editApplicantDescriptor.getName().orElse(applicantToEdit.getName());
        Phone updatedPhone = editApplicantDescriptor.getPhone().orElse(applicantToEdit.getPhone());
        Email updatedEmail = editApplicantDescriptor.getEmail().orElse(applicantToEdit.getEmail());
        Grade updatedGrade = editApplicantDescriptor.getGrade().orElse(applicantToEdit.getGrade());
        Institution updatedInstitution = editApplicantDescriptor.getInstitution()
                .orElse(applicantToEdit.getInstitution());
        GraduationYearMonth updatedGraduationYearMonth = editApplicantDescriptor.getGraduationYearMonth()
                .orElse(applicantToEdit.getGraduationYearMonth());
        Course updatedCourse = editApplicantDescriptor.getCourse().orElse(applicantToEdit.getCourse());
        ApplicationStatus updatedStatus = editApplicantDescriptor.getApplicationStatus()
                .orElse(applicantToEdit.getApplicationStatus());
        Set<Skill> updatedSkills = editApplicantDescriptor.getTags().orElse(applicantToEdit.getTags());


        return new Applicant(updatedName, updatedPhone, updatedEmail,
                updatedGrade, updatedInstitution, updatedCourse,
                updatedGraduationYearMonth, updatedStatus, updatedSkills);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editApplicantDescriptor.equals(e.editApplicantDescriptor);
    }

    public String toString() {
        return editApplicantDescriptor.toString();
    }

    /**
     * Stores the details to edit the applicant with. Each non-empty field value will replace the
     * corresponding field value of the applicant.
     */
    public static class EditApplicantDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Grade grade;
        private Institution institution;
        private GraduationYearMonth graduationYearMonth;
        private Course course;
        private ApplicationStatus status;
        private Set<Skill> skills;

        public EditApplicantDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditApplicantDescriptor(EditApplicantDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setGrade(toCopy.grade);
            setInstitution(toCopy.institution);
            setGraduationYearMonth(toCopy.graduationYearMonth);
            setCourse(toCopy.course);
            setApplicationStatus(toCopy.status);
            setTags(toCopy.skills);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, grade, institution, status, skills);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setGrade(Grade grade) {
            this.grade = grade;
        }

        public Optional<Grade> getGrade() {
            return Optional.ofNullable(grade);
        }

        public void setGraduationYearMonth(GraduationYearMonth graduationYearMonth) {
            this.graduationYearMonth = graduationYearMonth;
        }

        public Optional<GraduationYearMonth> getGraduationYearMonth() {
            return Optional.ofNullable(graduationYearMonth);
        }

        public void setInstitution(Institution institution) {
            this.institution = institution;
        }

        public Optional<Institution> getInstitution() {
            return Optional.ofNullable(institution);
        }

        public void setCourse(Course course) {
            this.course = course;
        }

        public Optional<Course> getCourse() {
            return Optional.ofNullable(course);
        }

        public void setApplicationStatus(ApplicationStatus status) {
            this.status = status;
        }

        public Optional<ApplicationStatus> getApplicationStatus() {
            return Optional.ofNullable(status);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Skill> skills) {
            this.skills = (skills != null) ? new HashSet<>(skills) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Skill>> getTags() {
            return (skills != null) ? Optional.of(Collections.unmodifiableSet(skills)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditApplicantDescriptor)) {
                return false;
            }

            // state check
            EditApplicantDescriptor e = (EditApplicantDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getGrade().equals(e.getGrade())
                    && getInstitution().equals(e.getInstitution())
                    && getGraduationYearMonth().equals(e.getGraduationYearMonth())
                    && getCourse().equals(e.getCourse())
                    && getApplicationStatus().equals(e.getApplicationStatus())
                    && getTags().equals(e.getTags());
        }

        @Override
        public String toString() {
            return "EditPersonDescriptor{"
                    + "name=" + name
                    + ", phone=" + phone
                    + ", email=" + email
                    + ", grade=" + grade
                    + ", institution=" + institution
                    + ", graduation year month=" + graduationYearMonth
                    + ", course=" + course
                    + ", tags=" + skills + '}';
        }
    }
}
