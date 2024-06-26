#!/usr/bin/env bash

# Copyright (C) 2023 GIP-RECIA, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


docker run --rm -it -v ${PWD}:/src ghcr.io/google/addlicense -f ./etc/header.template $(find ./src/ ./scripts/ -name '*.vue' -type f -or -name '*.ts' -type f -or -name '*.scss' -type f -or -name '*.java' -type f -or -name '*.xml' -type f -or -name '*.sh' -type f)
