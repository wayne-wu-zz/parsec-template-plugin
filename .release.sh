TRAVIS_BRANCH=$1
MASTER="master"

if [ "$TRAVIS_BRANCH" == "$MASTER" ] ; then
    ./gradlew :check
else
    echo "This is not the master branch" 
    echo "This is $TRAVIS_BRANCH"
fi
