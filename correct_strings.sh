#!/bin/bash

mydir="$(dirname "$(realpath "$0")")"

pushd "$mydir" > /dev/null

# Require clean git state
uncommitted=`git status --porcelain`
if [ ! -z "$uncommitted" ]; then
    echo "Uncommitted changes are present, please commit first!"
    exit 1
fi

mydir="."

# SchildiChat -> Chaton
find "$mydir/vector/src/main/res" -name strings.xml -exec \
    sed -i 's|SchildiChat|Chaton|g' '{}' \;
# Restore Element where it makes sense
find "$mydir/vector/src/main/res" -name strings.xml -exec \
    sed -i 's/Chaton \(Web\|iOS\|Desktop\)/SchildiChat \1/g' '{}' \;
find "$mydir/vector/src/main/res" -name strings.xml -exec \
    sed -i 's|Chaton Matrix Services|SchildiChat Matrix Services|g' '{}' \;
find "$mydir/vector/src/main/res" -name strings.xml -exec \
    sed -i 's|\("use_latest_riot">.*\)Chaton\(.*</string>\)|\1SchildiChat\2|g' '{}' \;
find "$mydir/vector/src/main/res" -name strings.xml -exec \
    sed -i 's|\("use_other_session_content_description">.*\)Chaton\(.*Chaton.*</string>\)|\1Chaton/SchildiChat\2|' '{}' \;

# Requires manual intervention for correct grammar
sed -i 's|!nnen|wolpertinger|g' "$mydir/vector/src/main/res/values-de/strings.xml"
sed -i 's|!n|schlumpfwesen|g' "$mydir/vector/src/main/res/values-de/strings.xml"

# Remove Triple-T stuff to avoid using them in F-Droid
rm -rf "$mydir/vector/src/main/play/listings"

git add -A
git commit -m "Automatic Chaton string correction"

popd > /dev/null

mydir="$(dirname "$(realpath "$0")")"
echo -e "\033[1;33m""Please fix -schlumpfwesen and -wolpertinger cases manually!""\033[0m"
echo "- $mydir/vector/src/main/res/values-de/strings.xml"
