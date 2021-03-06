echo "decrypting secring.gpg..."
openssl aes-256-cbc -K $encrypted_a89191c56639_key -iv $encrypted_a89191c56639_iv -in $TRAVIS_BUILD_DIR/secring.gpg.enc -out $TRAVIS_BUILD_DIR/secring.gpg -d

echo "publish archives & closeAndPromoteRepository..."
./gradlew build publishToNexus closeAndReleaseRepository -Psigning.keyId=$signingKeyId -Psigning.password=$secretKeyPassword -Psigning.secretKeyRingFile=$TRAVIS_BUILD_DIR/secring.gpg --info --stacktrace
