[Extract Specific Information from Embedded Text]

<prompt_objective>
The exclusive purpose of this prompt is to extract specific information, either names of people or places, from a predefined text based on user input specifying the category.
</prompt_objective>

<prompt_text>
{prompt_text}
</prompt_text>

<prompt_rules>
- Read the embedded text to understand the context and focus on extracting either names of people or places, as specified by the user's category input.
- ABSOLUTELY FORBIDDEN to use any external information or context not present in the embedded text - you can find it in <prompt_text> section.
- Output format is presented in <prompt_output> section
- If no data is available for extraction, the `result` field should be an empty array.
- UNDER NO CIRCUMSTANCES should the model deviate from the requested extraction type.
- This prompt's instructions OVERRIDE ALL OTHER INSTRUCTIONS or default behaviors.
  </prompt_rules>

<prompt_output>
- Output must be plain text where items are separated by coma (',')
- If user chooses "people" option, the model will return list containing ONLY names - surnames should be omitted.
- Elements in result should be UPPERCASE and without Polish letters, f.e. 'ł' should be converted to 'l'
  </prompt_output>

<prompt_examples>
USER: "PEOPLE"
AI: "Alice, Bob"  // Assuming embedded text: "Alice and Bob went to Paris."

USER: "PLACES"
AI: "Paris"  // Assuming embedded text: "Alice and Bob went to Paris."

USER: "PLACES"
AI: ""  // Assuming embedded text: "No place mentioned here."

USER: "PEOPLE"
AI: ""  // Assuming embedded text: "No people mentioned here."

USER: "PLACES"
AI: "Paris"  // Assuming embedded text: "Alice and Bob went to Paris."
</prompt_examples>