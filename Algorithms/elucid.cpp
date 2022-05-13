#include <iostream>
#include <string>
#include <algorithm>

// Function declaration
int elucid(float x, float y);

int main() {
    int a = elucid(100, 20);
    std::cout << a << std::endl;
    system("pause");
}

// Function definition
// Given two numbers, return the great common divisor of the two numbers
int elucid(float x, float y) {
    int large = std::max(x, y);
    int small = std::min(x, y);   
    int remainder = large % small;
    if (remainder == 0) {
        return small;
    }
    return elucid(remainder, small);
}