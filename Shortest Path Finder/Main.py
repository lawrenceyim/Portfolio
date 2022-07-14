import sys
import pygame
import queue
import time


def read_maze_from_file(file_name):
    try:
        with open(file_name, "r") as file:
            maze = []
            for line in file.readlines():
                maze.append(line)
            return maze
    except Exception:
        print("Invalid file")
        sys.exit()


def find_start(maze, start):
    for r, row in enumerate(maze):
        for c, value in enumerate(row):
            if value == start:
                return r, c
    return None


def find_path(maze, screen):
    start_pos = find_start(maze, start)

    q = queue.Queue()
    q.put((start_pos, [start_pos]))
    
    visited = set()

    while not q.empty():
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()

        current_pos, path = q.get()
        row, column = current_pos

        screen.fill((0, 0, 0))
        print_maze(maze, screen, path)
        time.sleep(0.2)

        if maze[row][column] == end:
            return path
        
        neighbors = find_neighbors(maze, row, column)

        for neighbor in neighbors:
            if neighbor not in visited:
                r, c = neighbor
                if maze[r][c] != wall:
                    new_path = path + [neighbor]
                    q.put((neighbor, new_path))
                    visited.add(neighbor)
    
    
        
def find_neighbors(maze, row, col):
    neighbors = []
    
    if row > 0:
        neighbors.append((row - 1, col))
    if row < len(maze):
        neighbors.append((row + 1, col))
    if col > 0:
        neighbors.append((row, col - 1))
    if col < len(maze[0]):
        neighbors.append((row, col + 1))

    return neighbors


def print_maze(maze, screen, path=[]):
    for r, row in enumerate(maze):
        for c, _ in enumerate(row):
            if (r, c) in path:
                pygame.draw.rect(screen, RED, pygame.Rect(c * square_length, r * square_length, square_length, square_length))
            elif maze[r][c] == wall:
                pygame.draw.rect(screen, BLUE, pygame.Rect(c * square_length, r * square_length, square_length, square_length))
    pygame.display.update()
            

maze = read_maze_from_file("maze1.txt")
rows = len(maze)
columns = len(maze[0])
square_length = 20

# Columns indicate X-axis, rows indicate Y-axis
screen_size = (square_length * columns, square_length * rows)

# RGB colors
RED = (255, 0, 0)
BLUE = (0, 0, 255)

# Values in the maze
wall = "0"
path = "1"
start = "2"
end = "3"


pygame.init()
screen = pygame.display.set_mode(screen_size)
pygame.display.set_caption("Shortest Path Finder")

find_path(maze, screen)
while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            exit()
    pygame.display.update()
