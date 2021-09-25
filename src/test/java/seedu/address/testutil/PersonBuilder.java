package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Course;
import seedu.address.model.person.Email;
import seedu.address.model.person.Grade;
import seedu.address.model.person.GraduationYearMonth;
import seedu.address.model.person.Institution;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.skills.Skill;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_GRADE = "4.50";
    public static final String DEFAULT_INSTITUTION = "NTU";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String DEFAULT_GRADUATIONYEARMONTH = "06/2024";
    public static final String DEFAULT_COURSE = "Computer Science";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Grade grade;
    private Institution institution;
    private GraduationYearMonth graduationYearMonth;
    private Course course;
    private Set<Skill> skills;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        grade = new Grade(DEFAULT_GRADE);
        institution = new Institution(DEFAULT_INSTITUTION);
        graduationYearMonth = new GraduationYearMonth(DEFAULT_GRADUATIONYEARMONTH);
        course = new Course(DEFAULT_COURSE);
        skills = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        grade = personToCopy.getGrade();
        institution = personToCopy.getInstitution();
        graduationYearMonth = personToCopy.getGraduationYearMonth();
        course = personToCopy.getCourse();
        skills = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.skills = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Grade} of the {@code Person} that we are building.
     */
    public PersonBuilder withGrade(String grade) {
        this.grade = new Grade(grade);
        return this;
    }

    /**
     * Sets the {@code Institution} of the {@code Person} that we are building.
     */
    public PersonBuilder withInstitution(String institution) {
        this.institution = new Institution(institution);
        return this;
    }
    /**
     * Sets the {@code GraduationYearMonth} of the {@code Person} that we are building.
     */
    public PersonBuilder withGraduationYearMonth(String graduationYearMonth) {
        this.graduationYearMonth = new GraduationYearMonth(graduationYearMonth);
        return this;
    }

    /**
     * Sets the {@code Course} of the {@code Person} that we are building.
     */
    public PersonBuilder withCourse(String course) {
        this.course = new Course(course);
        return this;
    }


    public Person build() {
        return new Person(name, phone, email, address, grade, institution, course, graduationYearMonth, skills);
    }
}
