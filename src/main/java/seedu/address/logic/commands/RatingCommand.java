package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_RESTAURANTS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.restaurant.Rating;
import seedu.address.model.restaurant.Restaurant;

/**
 * Provide rating for restaurants in the address book.
 */
public class RatingCommand extends Command {
    public static final String COMMAND_WORD = "rating";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Set or edit the rating of the restaurant identified "
            + "by the index number used in the last restaurant listing. "
            + "Existing rating will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_RATING + "[RATING]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_RATING + "10";

    public static final String MESSAGE_ADD_RATING_SUCCESS = "Added rating to Restaurant: %1$s";
    public static final String MESSAGE_DELETE_RATING_SUCCESS = "Removed rating from Restaurant: %1$s";

    private final Index index;
    private final Rating rating;

    /**
     * @param index of the restaurant in the filtered restaurant list to edit the rating
     * @param rating of the restaurant to be updated to
     */
    public RatingCommand(Index index, Rating rating) {
        requireAllNonNull(index, rating);

        this.index = index;
        this.rating = rating;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }

        Restaurant restaurantToEdit = lastShownList.get(index.getZeroBased());
        Restaurant editedRestaurant = new Restaurant(restaurantToEdit.getName(),
                restaurantToEdit.getPhone(), restaurantToEdit.getEmail(),
                restaurantToEdit.getAddress(), rating, restaurantToEdit.getTags());

        model.setRestaurant(restaurantToEdit, editedRestaurant);
        model.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);

        return new CommandResult(generateSuccessMessage(editedRestaurant));
    }

    /**
     * Generates a command execution success message based on whether the rating is added to or removed from
     * {@code restaurantToEdit}.
     */
    private String generateSuccessMessage(Restaurant restaurantToEdit) {
        return String.format(MESSAGE_ADD_RATING_SUCCESS, restaurantToEdit);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RatingCommand)) {
            return false;
        }

        // state check
        RatingCommand e = (RatingCommand) other;
        return index.equals(e.index)
                && rating.equals(e.rating);
    }
}