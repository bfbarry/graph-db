
fn = "/Users/brianbarry/Desktop/computing/graph-db-project/data.bin";
with open(fn, 'rb') as f:
    i = f.read()
    print(i)
    print(len(i))