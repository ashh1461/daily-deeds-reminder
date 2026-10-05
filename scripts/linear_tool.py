import sys
import json
import urllib.request
import os

def get_api_key():
    if "LINEAR_API_KEY" in os.environ and os.environ["LINEAR_API_KEY"]:
        return os.environ["LINEAR_API_KEY"]
    token_file = os.path.join(os.path.dirname(__file__), "..", ".linear_token")
    if os.path.exists(token_file):
        with open(token_file, "r", encoding="utf-8") as f:
            return f.read().strip()
    return ""

API_KEY = get_api_key()
LINEAR_ENDPOINT = "https://api.linear.app/graphql"

def query_linear(query, variables=None):
    headers = {
        "Content-Type": "application/json",
        "Authorization": API_KEY
    }
    data = json.dumps({"query": query, "variables": variables or {}}).encode("utf-8")
    req = urllib.request.Request(LINEAR_ENDPOINT, data=data, headers=headers)
    try:
        with urllib.request.urlopen(req) as resp:
            return json.loads(resp.read().decode("utf-8"))
    except urllib.error.HTTPError as e:
        print(f"HTTP Error: {e.code} - {e.read().decode('utf-8')}", file=sys.stderr)
        sys.exit(1)
    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)

def teams():
    query = """
    query {
      teams {
        nodes {
          id
          name
          key
          states {
            nodes {
              id
              name
              type
            }
          }
        }
      }
    }
    """
    res = query_linear(query)
    print(json.dumps(res, indent=2))

def create_issue(team_id, title, description, state_id=None):
    query = """
    mutation CreateIssue($input: IssueCreateInput!) {
      issueCreate(input: $input) {
        success
        issue {
          id
          identifier
          title
          url
        }
      }
    }
    """
    input_data = {
        "teamId": team_id,
        "title": title,
        "description": description
    }
    if state_id:
        input_data["stateId"] = state_id
    res = query_linear(query, {"input": input_data})
    print(json.dumps(res, indent=2))

def comment(issue_id, body):
    query = """
    mutation CreateComment($input: CommentCreateInput!) {
      commentCreate(input: $input) {
        success
        comment {
          id
          body
        }
      }
    }
    """
    res = query_linear(query, {"input": {"issueId": issue_id, "body": body}})
    print(json.dumps(res, indent=2))

def update_issue(issue_id, state_id=None, title=None, description=None):
    query = """
    mutation UpdateIssue($id: String!, $input: IssueUpdateInput!) {
      issueUpdate(id: $id, input: $input) {
        success
        issue {
          id
          identifier
          state {
            name
            type
          }
        }
      }
    }
    """
    input_data = {}
    if state_id:
        input_data["stateId"] = state_id
    if title:
        input_data["title"] = title
    if description:
        input_data["description"] = description
    res = query_linear(query, {"id": issue_id, "input": input_data})
    print(json.dumps(res, indent=2))

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: linear_tool.py <teams|create|comment|update> [args...]")
        sys.exit(1)
    
    cmd = sys.argv[1]
    if cmd == "teams":
        teams()
    elif cmd == "create":
        team_id = sys.argv[2]
        title = sys.argv[3]
        desc = sys.argv[4] if len(sys.argv) > 4 else ""
        state_id = sys.argv[5] if len(sys.argv) > 5 else None
        create_issue(team_id, title, desc, state_id)
    elif cmd == "comment":
        issue_id = sys.argv[2]
        body = sys.argv[3]
        comment(issue_id, body)
    elif cmd == "update":
        issue_id = sys.argv[2]
        state_id = sys.argv[3] if len(sys.argv) > 3 else None
        update_issue(issue_id, state_id=state_id)
