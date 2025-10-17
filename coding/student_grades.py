students = [
    {"name": "Alice", "marks": 95},
    {"name": "Bob", "marks": 85},
    {"name": "Charlie", "marks": 75},
    {"name": "David", "marks": 65},
    {"name": "Eve", "marks": 55},
]

# Define categories
def get_category(marks):
    if marks > 80:
        return "Excellent"
    elif 70 <= marks <= 80:
        return "Good"
    elif 60 <= marks < 70:
        return "Nice"
    elif 50 <= marks < 60:
        return "Average"
    else:
        return "Poor"

# Categorize students
category_map = {}
for student in students:
    cat = get_category(student["marks"])
    if cat not in category_map:
        category_map[cat] = []
    category_map[cat].append(student)

# Define category order for tie-breaking
category_order = ["Excellent", "Good", "Nice", "Average", "Poor"]

# Sorting key function
def sort_key(item):
    category, stu_list = item
    # Return tuple: (-number of students, category index)
    return (-len(stu_list), category_order.index(category))

# Sort categories
sorted_categories = sorted(category_map.items(), key=sort_key)

# Print results
for cat, stu_list in sorted_categories:
    print(f"{cat} ({len(stu_list)} students):")
    for s in stu_list:
        print(f"  {s['name']} - {s['marks']}")
    print("-" * 30)
