package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalApplicants.getTypicalInternWatcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Applicant;
import seedu.address.testutil.ApplicantBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalInternWatcher(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Applicant validApplicant = new ApplicantBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validApplicant);

        assertCommandSuccess(new AddCommand(validApplicant), model,
                String.format(AddCommand.MESSAGE_SUCCESS, validApplicant), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Applicant applicantInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddCommand(applicantInList), model, AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
