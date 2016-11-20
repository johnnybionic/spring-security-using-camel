package org.johnnybionic.custom.custom;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsCollectionContaining;

/**
 * Custom Hamcrest matcher(s).
 *
 * @author johnny
 *
 */
public class CustomMatchers {

    /**
     * Tests if the item to be matched occurs exactly n times in the collection.
     *
     * @param n the number of times this element is expected to occur
     * @param elementMatcher the matcher
     * @return a {@link Matcher} that iterates over a collection of type T and
     *         its supertypes
     */
    public static <T> Matcher<Iterable<? super T>> exactlyNItems(final int n, Matcher<T> elementMatcher) {

        IsCollectionContaining<T> isCollectionContaining = new IsCollectionContaining<T>(elementMatcher) {

            @Override
            protected boolean matchesSafely(Iterable<? super T> collection, Description mismatchDescription) {
                int count = 0;
                boolean isPastFirst = false;

                for (Object item : collection) {

                    if (elementMatcher.matches(item)) {
                        count++;
                    }

                    if (isPastFirst) {
                        mismatchDescription.appendText(", ");
                    }

                    elementMatcher.describeMismatch(item, mismatchDescription);
                    isPastFirst = true;
                }

                if (count != n) {
                    mismatchDescription.appendText(". Expected exactly " + n + " but got " + count);
                }

                return count == n;
            }
        };

        return isCollectionContaining;
    }

    /**
     * Convenience method to test an element occurs only once.
     *
     * @param elementMatcher the matcher
     * @return a {@link Matcher} that iterates over a collection of type T and
     *         its supertypes
     */
    public static <T> Matcher<Iterable<? super T>> exactlyOnce(Matcher<T> elementMatcher) {
        return exactlyNItems(1, elementMatcher);
    }

}
