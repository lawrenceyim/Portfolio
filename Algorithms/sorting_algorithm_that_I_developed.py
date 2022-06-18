# Helper function for sorting algorithm that I developed
def binaryInsert(keys, num):
    if len(keys) == 0:
        keys.append(num)
        return keys
    low = 0
    high = len(keys) - 1
    while low <= high:
        mid = low + (high - low) // 2
        if keys[mid] > num:
            high = mid - 1
        elif keys[mid] < num:
            low = mid + 1
    keys.insert(low, num)
    return keys


# Sorting algorithm that I developed
def newsort(nums):
    dict = {}
    keys = []
    for num in nums:
        if num not in dict:
            dict[num] = 1
            keys = binaryInsert(keys, num)
        else:
            dict[num] += 1
    index = 0
    for key in keys:
        for i in range(dict[key]):
            nums[index] = key
            index += 1
    return nums