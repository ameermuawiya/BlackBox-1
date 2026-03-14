#ifndef PINE_SCOPED_LOCAL_REF_H
#define PINE_SCOPED_LOCAL_REF_H

#include "macros.h"
#include <cstddef>

template<typename T>
class ScopedLocalRef {
public:
    /*
     * Initializes the reference with the given JNI environment.
     */
    ScopedLocalRef(JNIEnv* env) : env(env), mLocalRef(nullptr) {
    }

    /*
     * Initializes the reference with the environment and an existing reference.
     */
    ScopedLocalRef(JNIEnv* env, T ref) : env(env), mLocalRef(ref) {
    }

    /*
     * Releases the local reference upon destruction.
     */
    ~ScopedLocalRef() {
        Reset();
    }

    /*
     * Returns the underlying local reference.
     */
    T Get() const {
        return mLocalRef;
    }

    /*
     * Deletes the current reference and assigns a new one.
     */
    void Reset(T newRef = nullptr) {
        if (mLocalRef != newRef) {
            if (mLocalRef != nullptr) {
                env->DeleteLocalRef(mLocalRef);
            }
            mLocalRef = newRef;
        }
    }

    /*
     * Releases ownership of the local reference without deleting it.
     */
    T Release() __attribute__((warn_unused_result)) {
        T ref = mLocalRef;
        mLocalRef = nullptr;
        return ref;
    }

    /*
     * Checks if the underlying reference is null.
     */
    bool IsNull() const {
        return mLocalRef == nullptr;
    }

    /*
     * Returns the JNI environment associated with this reference.
     */
    JNIEnv* Env() {
        return env;
    }

    /*
     * Checks equality with nullptr.
     */
    bool operator==(std::nullptr_t) const {
        return IsNull();
    }

    /*
     * Checks inequality with nullptr.
     */
    bool operator!=(std::nullptr_t) const {
        return !IsNull();
    }

    /*
     * Checks if two scoped references point to the same object.
     */
    bool operator==(ScopedLocalRef const s) const {
        return env->IsSameObject(mLocalRef, s.mLocalRef);
    }

    /*
     * Checks if two scoped references point to different objects.
     */
    bool operator!=(ScopedLocalRef const s) const {
        return !env->IsSameObject(mLocalRef, s.mLocalRef);
    }

    /*
     * Checks if the reference points to the same object as another reference.
     */
    bool operator==(T const other) const {
        return env->IsSameObject(mLocalRef, other);
    }

    /*
     * Checks if the reference points to a different object than another reference.
     */
    bool operator!=(T const other) const {
        return !env->IsSameObject(mLocalRef, other);
    }

private:
    JNIEnv* env;
    T mLocalRef;

    DISALLOW_COPY_AND_ASSIGN(ScopedLocalRef);
};


class ScopedLocalClassRef : public ScopedLocalRef<jclass> {
public:
    /*
     * Initializes an empty class reference.
     */
    ScopedLocalClassRef(JNIEnv* env) : ScopedLocalRef<jclass>(env) {
    }

    /*
     * Initializes with an existing class reference.
     */
    ScopedLocalClassRef(JNIEnv* env, jclass ref) : ScopedLocalRef<jclass>(env, ref) {
    }

    /*
     * Initializes by finding a class with the given name.
     */
    ScopedLocalClassRef(JNIEnv* env, const char* name) : ScopedLocalRef<jclass>(env, env->FindClass(name)) {
    }

    /*
     * Finds and returns a method ID for the current class.
     */
    jmethodID FindMethodID(const char* name, const char* signature) {
        JNIEnv* env = Env();
        jmethodID method = env->GetMethodID(Get(), name, signature);
        if (LIKELY(method != nullptr)) {
            return method;
        } else {
            env->ExceptionClear();
            return nullptr;
        }
    }
};

class ScopedLocalUtfStringRef : public ScopedLocalRef<jstring> {
public:
    /*
     * Initializes an empty string reference.
     */
    ScopedLocalUtfStringRef(JNIEnv* env) : ScopedLocalRef<jstring>(env) {
    }

    /*
     * Initializes with an existing string reference.
     */
    ScopedLocalUtfStringRef(JNIEnv* env, jstring ref) : ScopedLocalRef<jstring>(env, ref) {
    }

    /*
     * Initializes by creating a new string from UTF characters.
     */
    ScopedLocalUtfStringRef(JNIEnv* env, const char* content) : ScopedLocalRef<jstring>(
            env, env->NewStringUTF(content)) {
    }
};

#endif //PINE_SCOPED_LOCAL_REF_H
