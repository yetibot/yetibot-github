# yetibot-github

A [Yetibot](https://yetibot.com) plugin for [GitHub](https://github.com).

## Configuration

```clojure
{:yetibot
 {:github
  {:endpoint "" ;; optional - override this if using GH enterprise
   :org ["" ""] ;; default orgs
   :token "" ;; your github api token
   }}}
```

## Usage

```
!help gh

gh pr <org-name> # list open pull requests for <org-name>
gh search repos <query> # search GitHub repos for <query>
gh topics <org-name>/<repo> # list topics for a repo
gh search <query> # search GitHub code for <query>
gh incidents # show all recent GitHub system status messages
gh tags <org-name>/<repo> # list the tags for <org-name>/<repo>
gh search topics <query> # search GitHub topics for <query>
gh feed <org-name> # list recent activity for <org-name>
gh branches <org-name>/<repo> # list branches for <org-name>/<repo>
gh repos # list repos for the first configured org
gh repos <org-name> # list repos under <org-name>
gh contributors <org>/<repo-name> since <n> <minutes|hours|days|weeks|months> ago # list contributors in order of commits since a given time
gh releases show <org>/<repo-name> <tag> # retrieve info about a specific release tag on a Github repository
gh stats <org>/<repo-name> # commits, additions, deletions
gh releases <org>/<repo-name> # list releases for a Github repository
gh releases show <org>/<repo-name> # retrieve info about the latest release on a Github repository
gh status # show GitHub's current system status
gh topics set <owner>/<repo> <collection or space-separated list of topics>
gh orgs # show configured orgs
```

## License

Copyright © 2020 Trevor Hartman

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
