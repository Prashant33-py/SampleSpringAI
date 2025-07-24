## General Instructions ##
- For using/running an LLM model, you need to download and install Ollama on your system.
- Once that is done, now based on what your system can handle, download and run the LLM model locally using the command` ollama run <modelname> `in your command prompt
- Now just run your Spring boot application and try sending a GET request with any prompt to the endpoint [ http://localhost:8080/api/ollama-ai ](http://localhost:8080/api/ollama-ai)
- The request body should be in JSON format like this:
```json
{
  "question": "<Your prompt here>"
}
```

Embeddings: Numerical representation of data that are designed to allow machine learning models to understand and relate to the data in a more meaningful way. In this case, embeddings are used to represent the text data in a way that can be processed by the LLM model.