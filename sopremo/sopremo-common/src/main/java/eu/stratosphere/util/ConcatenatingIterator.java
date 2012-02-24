package eu.stratosphere.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Concatenates several iterators to one large iterator.<br>
 * In the beginning, all elements of the first iterator are successively returned. If the first iterator is empty, the
 * elements of the second iterator are streamed and so on.
 * 
 * @author Arvid Heise
 * @param <T>
 *        the element type
 */
public final class ConcatenatingIterator<T> extends AbstractIterator<T> {
	private final Iterator<? extends Iterator<? extends T>> inputs;

	@SuppressWarnings("unchecked")
	private Iterator<? extends T> currentIterator = Collections.EMPTY_LIST.iterator();

	/**
	 * Initializes a ConcatenatingIterator with an array of iterators. This constructor is not type-safe.
	 * 
	 * @param iterators
	 *        the iterators to concatenate
	 */
	public ConcatenatingIterator(final Iterator<T>... iterators) {
		this.inputs = Arrays.asList(iterators).iterator();
	}

	public ConcatenatingIterator(final Iterator<? extends Iterator<? extends T>> iterators) {
		this.inputs = iterators;
	}

	/**
	 * Initializes a type-safe ConcatenatingIterator with a list of iterators.
	 * 
	 * @param iterators
	 *        the iterators to concatenate
	 */
	public ConcatenatingIterator(final List<? extends Iterator<? extends T>> iterators) {
		this.inputs = iterators.iterator();
	}

	@Override
	protected T loadNext() {
		boolean curHasNext;
		while ((curHasNext = currentIterator.hasNext()) || this.inputs.hasNext()) {
			if (curHasNext)
				return currentIterator.next();
			currentIterator = this.inputs.next();
		}
		return this.noMoreElements();
	}
}