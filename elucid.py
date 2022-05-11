# Elucid algorithm that finds the greatest common divisor of two numbers
def elucid(number1, number2):
    # Identify the larger and the smaller of the two numbers
    larger = max(number1, number2)
    smaller = min(number1, number2)

    # Divide the larger number by the smaller number to determine if the smaller number
    # is the greatest common divisor
    remainder = larger % smaller
    if remainder == 0:
        return smaller
    else:
        return elucid(smaller, remainder) # Recursively call the Elucid algorithm


# Test the Elucid algorithm
print(elucid(5, 15))
print(elucid(10532, 1535))
print(elucid(24, 16))
print(elucid(24, 6))