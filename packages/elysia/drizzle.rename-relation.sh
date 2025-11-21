#!/bin/bash

#
# このスクリプトは、drizzle.rename-relation.sh のリレーション関連の識別子を sed を使って一括で置換します。
#
# 置換ルール:
# - user_creatorId -> creator
# - user_assigneeId -> assignee
# - todos_creatorId -> createdTodos
# - todos_assigneeId -> assignedTodos
# - relationName: 'todos_creatorId_users_id' -> relationName: 'todos_creator'
# - relationName: 'todos_assigneeId_users_id' -> relationName: 'todos_assignee'
#

# 変更対象のファイル
TARGET_FILE="/workspace/packages/elysia/src/db/relations.ts"

# sed コマンドで一括置換
sed -i \
  -e "s/user_creatorId/creator/g" \
  -e "s/user_assigneeId/assignee/g" \
  -e "s/todos_creatorId: many(todos, {/createdTodos: many(todos, {/g" \
  -e "s/todos_assigneeId: many(todos, {/assignedTodos: many(todos, {/g" \
  -e "s/relationName: \"todos_creatorId_users_id\"/relationName: \"todos_creator\"/g" \
  -e "s/relationName: \"todos_assigneeId_users_id\"/relationName: \"todos_assignee\"/g" \
  "$TARGET_FILE"
