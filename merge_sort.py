# Merge sort algorithm that takes a list of numbers and returns a sorted list
def merge_sort(list):
    # Base case of when the list has a length of one or zero
    if len(list) <= 1:
        return list
    
    # Divide the length of the list in half
    half = len(list) // 2
    
    # Recursively call the merge sort algorithm using dynamic programming paradigm
    a = merge_sort(list[:half])
    b = merge_sort(list[half:])
    
    # Instantiate a new list to contain the sorted results
    c = []
    
    # Instantiate counter variables for each of the lists
    alen = 0
    blen = 0
    clen = 0
    
    # Transfer values from the results of the recursive function calls to the new list
    while (alen < len(a) and blen < len(b)):
        if (a[alen] < b[blen]):
            c.append(a[alen])
            alen += 1
            clen += 1
        else:
            c.append(b[blen])
            blen += 1
            clen += 1
            
    # Transfer the remaining values from the results of the recursive call to the new list 
    while (alen < len(a)):
        c.append(a[alen])
        alen += 1
        clen += 1
    while (blen < len(b)):
        c.append(b[blen])
        blen += 1
        clen += 1

    return c


# Testing the merge sort algorithm
a = [1, 5, 2, -6, -3, 2, 1, 6, 62, 117, 721, 4902, 1, 61, 0, -161]
print(a)
a = merge_sort(a)
print(a)
