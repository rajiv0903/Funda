package course02._09._05_heapify.practice;

public class MinHeapPractice<T extends Comparable<T>> extends HeapPractice<T> 
{
	
	public MinHeapPractice(Class<T> clazz) {
		super(clazz);
	}

	public MinHeapPractice(Class<T> clazz, int size) {
		super(clazz, size);
	}

	@Override
	protected void siftDown(int index) 
	{
		int leftIndex = getLeftChildIndex(index);
		int rightIndex = getRightChildIndex(index);
		
		// Find the minimum of the left and right child elements.
        int smallerIndex = -1;
        
        if(leftIndex != -1 && rightIndex != -1)
        {
        	smallerIndex = getElementAtIndex(leftIndex).compareTo(getElementAtIndex(rightIndex)) < 0? leftIndex : rightIndex;
        }
        else if(leftIndex != -1)
        {
        	smallerIndex = leftIndex;
        }
        else if(rightIndex != -1)
        {
        	smallerIndex = rightIndex;
        }
        if(smallerIndex == -1)
        {
        	return;
        }
        // Compare the smaller child with the current index to see if a swap
        // and further sift down is needed.
        if (getElementAtIndex(smallerIndex).compareTo(getElementAtIndex(index)) < 0) 
        {
            swap(smallerIndex, index);
            siftDown(smallerIndex);
        }

	}

	@Override
	protected void siftUp(int index) 
	{
		 int parentIndex = getParentIndex(index);
		 
		 if (parentIndex != -1 &&
	                getElementAtIndex(index).compareTo(getElementAtIndex(parentIndex)) < 0) 
		 {
	            swap(parentIndex, index);

	            siftUp(parentIndex);
	     }
	}

}
