text_data = """
Listners1:
Active : yes
Connection : 234
Next parame : 492
Jdk: 4920

Listners2:
Active : yes
Connection : 48399
Next parame : 53
Jdk: 4920
"""

def extract_listeners_connections(text):
    listeners = {}
    current_listener = None
    
    lines = text.strip().split('\n')
    
    for line in lines:
        line = line.strip()
        if not line:
            continue
            
        if line.endswith(':'):
            # New listener section
            current_listener = line[:-1]  # Remove the colon
            listeners[current_listener] = {}
        elif ':' in line:
            # Parameter line
            key, value = line.split(':', 1)
            key = key.strip()
            value = value.strip()
            
            if current_listener:
                listeners[current_listener][key] = value
    
    return listeners

# Extract data
listeners_data = extract_listeners_connections(text_data)

# Print each listener and its connection
for listener_name, params in listeners_data.items():
    connection = params.get('Connection', 'Not found')
    print(f"{listener_name}: Connection = {connection}")


## Output

Listners1: Connection = 234
Listners2: Connection = 48399