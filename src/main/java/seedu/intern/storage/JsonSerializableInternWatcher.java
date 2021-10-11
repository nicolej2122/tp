package seedu.intern.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.intern.commons.exceptions.IllegalValueException;
import seedu.intern.model.InternWatcher;
import seedu.intern.model.ReadOnlyInternWatcher;
import seedu.intern.model.applicant.Applicant;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableInternWatcher {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate applicant(s).";

    private final List<JsonAdaptedApplicant> applicant = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableInternWatcher(@JsonProperty("applicants") List<JsonAdaptedApplicant> applicant) {
        this.applicant.addAll(applicant);
    }

    /**
     * Converts a given {@code ReadOnlyInternWatcher} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableInternWatcher}.
     */
    public JsonSerializableInternWatcher(ReadOnlyInternWatcher source) {
        applicant.addAll(source.getPersonList().stream().map(JsonAdaptedApplicant::new).collect(Collectors.toList()));
    }

    /**
     * Converts this intern watcher into the model's {@code InternWatcher} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public InternWatcher toModelType() throws IllegalValueException {
        InternWatcher internWatcher = new InternWatcher();
        for (JsonAdaptedApplicant jsonAdaptedApplicant : applicant) {
            Applicant applicant = jsonAdaptedApplicant.toModelType();
            if (internWatcher.hasPerson(applicant)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            internWatcher.addPerson(applicant);
        }
        return internWatcher;
    }

}
