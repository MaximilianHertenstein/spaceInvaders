#!/usr/bin/env python3
import re
from pathlib import Path

root = Path(__file__).resolve().parents[1]
ws = root / 'worksheet.md'
test = root / 'src' / 'test' / 'java' / 'org' / 'example' / 'WorksheetTests.java'

if not ws.exists():
    print('worksheet.md not found')
    raise SystemExit(1)
if not test.exists():
    print('WorksheetTests.java not found')
    raise SystemExit(1)

ws_text = ws.read_text()
test_text = test.read_text()

# Extract example lines from worksheet.md — treat each non-empty line as an example if it contains '(' or 'new '
examples = []
for line in ws_text.splitlines():
    s = line.strip()
    if not s:
        continue
    # heuristics: example lines contain 'new ' or '(' or 'Utils.' or 'LevelFactory' etc
    if any(token in s for token in ('new ', 'Utils.', 'LevelFactory', 'AlienSwarm', 'Model', 'TUI', 'Controller', 'plasma', 'cannon', 'r.nextInt', 'Utils.repeat', 'getShootingAlien', 'getLowestAliens')):
        examples.append(s)

# Normalize examples (remove trailing semicolons)
examples = [e.rstrip(';') for e in examples]

missing = []
found = []
for ex in examples:
    # create a simple key: choose main identifier
    key = ex
    # check if any substring of ex appears in test_text
    if ex in test_text:
        found.append(ex)
    else:
        # try relaxed matching: class/method name present
        tokens = re.findall(r"[A-Za-z_][A-Za-z0-9_\\.]*", ex)
        matched = False
        for t in tokens[:3]:
            if t and t in test_text:
                matched = True
                break
        if matched:
            found.append(ex + '  (matched by token)')
        else:
            missing.append(ex)

print('Total examples parsed:', len(examples))
print('Found or matched:', len(found))
print('Missing:', len(missing))
print('\n--- Missing examples (need asserts) ---')
for m in missing:
    print(m)

# write report
report = root / 'worksheet_test_report.txt'
with report.open('w') as f:
    f.write(f'Total examples parsed: {len(examples)}\n')
    f.write(f'Found or matched: {len(found)}\n')
    f.write(f'Missing: {len(missing)}\n\n')
    f.write('Missing examples:\n')
    for m in missing:
        f.write(m + '\n')

print('\nReport written to', report)

