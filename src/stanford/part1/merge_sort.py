__author__ = 'Stepan.Furdey'

def merge_sort(arr = []) :
    if len(arr) == 1:
        return arr

    len1 = len(arr) // 2
    len2 = len(arr) - len1

    arr1 = arr[0:len1]
    arr2 = arr[len2:len(arr)]

    merge_sort(arr1)
    merge_sort(arr2)

    i = 0
    j = 0

    for k in range(len(arr)):
        if i >= len1:
            arr[k] = arr2[j]
            j += 1
        elif j >= len2:
            arr[k] = arr1[i]
            i += 1
        else:
            if arr1[i] < arr2[j]:
                arr[k] = arr1[i]
                i += 1
            else:
                arr[k] = arr2[j]
                j += 1

    return arr

array = [9, 4, 6, 2, 6, 8, 3, 1]
print("source: ", array)
array = merge_sort(array)
print("result: ", array)


